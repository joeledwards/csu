var stars = new Array();

var distance = 2000.0;
var refresh_rate = 25;
var add_delay = 100;
var maxId = 0;

var min_longevity = 5;
var max_longevity = 15;

var x_center;
var y_center;


var min_radius = 1;
var max_radius = 20;

var height;
var width;

var canvas;

var refreshing = false;

function repaintStar(context)
{
    if (context._new) {
        document.write("<div id=\"star-"+context._id+"\" style=\"position:absolute; background:white; left:"+x_center+"px; top:"+y_center+"px; width:1px; height:1px;\"></div>");
        context._new = false;
    }
    var star = document.getElementById("star-"+context._id);

    if (context._path_pos <= -1000) {
        document.body.removeChild(star);
        return false;
    }

    var radius = min_radius + (distance - context._path_pos) / distance * max_radius;

    var x_diff = context._x_pos - x_center;
    var y_diff = context._y_pos - y_center;

    var x_positive = (x_diff > 0) ? 1 : -1;
    var y_positive = (y_diff > 0) ? 1 : -1;

    var x_slant_ratio = Math.sqrt((distance * distance) - (x_diff * x_diff)) / distance;
    var y_slant_ratio = Math.sqrt((distance * distance) - (y_diff * y_diff)) / distance;

    var x_view = Math.sqrt(Math.pow(distance - context._path_pos, 2) - Math.pow(x_slant_ratio * (distance - context._path_pos), 2)) * x_positive;
    var y_view = Math.sqrt(Math.pow(distance - context._path_pos, 2) - Math.pow(y_slant_ratio * (distance - context._path_pos), 2)) * y_positive;

    star.style.top    = y_center + y_view - radius;
    star.style.left   = x_center + x_view - radius;
    star.style.height = 2 * radius;
    star.style.width  = 2 * radius;
    star.style.borderRadius = radius;

    context._path_pos -= radius;
    return true;
}

function refresh()
{
    if (refreshing) {
        return;
    }
    refreshing = true;

    for (var i in stars) {
        var context = stars[i];
        if (repaintStar(context) == false) {
            stars.splice(i,1);
        }
    }
    refreshing = false;
}

function addStar()
{
    var context = new Object;
    context._x_pos = Math.floor(Math.random() * width);
    context._y_pos = Math.floor(Math.random() * height);

    context._path_pos = distance;
    context._id = maxId++;
    context._new = true;
    stars.push(context);
}

function init()
{
    if (navigator.appName == "Microsoft Internet Explorer") {
        height = document.documentElement.clientHeight;
        width = document.documentElement.clientWidth;
    }
    else {
        height = window.innerHeight;
        width = window.innerWidth;
    }

    document.write("<div id=\"canvas\" style=\"background:black; position:absolute; left:0px; right:0px; width:"+width+"px; height:"+height+"px;\"></div>");
    canvas = document.getElementById("canvas");

    x_center = width / 2;
    y_center = height / 2;

    setInterval(function () {addStar();}, add_delay);
    setInterval(function () {refresh();}, refresh_rate);
}

