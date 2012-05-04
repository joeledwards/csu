var runner = undefined;
var images = new Array();
var leaves = new Array();
var interval = 100;
var intro_offset = 1417;
var maxId = 0;

var height;
var width;

function updateFrame(context)
{
    context._y_pos += context._fall_rate;
    document.getElementById("leaf-"+context._id+"-div").style.top = context._y_pos+"px";
    if (context._y_pos >= height) {
        clearInterval(context._interval);
    }
}

function addLeaf()
{
    var idx = Math.floor(Math.random() * images.length);
    var context = new Object;
    context._fall_rate = Math.floor(Math.random() * 15) + 5; // 5 - 20 pixels per 
    context._x_pos = Math.floor(Math.random() * (width - 50)) + 25;
    context._y_pos = -25;
    context._id = maxId++;

    document.write("<div id=\"leaf-"+context._id+"-div\" style=\"position:absolute; left:"+context._x_pos+"px; top:"+context._y_pos+"px\">");
    document.write("<img id=\"leaf-"+context._id+"-img\" src=\"\" />");
    document.write("</div>");

    document.getElementById("leaf-"+context._id+"-img").src = images[idx].src;

    context._interval = setInterval(function () {
            updateFrame(context);
            }, interval);
}

function init()
{
    if (navigator.appName == "Microsoft Internet Explorer") {
        height = document.documentElement.clientHeight;
        width = document.documentElement.clientWidth;
    }
    else {
        height = window.innerHeight;
        width = window.innerWidth - 14;
    }
    for (var i = 1; i <= 6; i++) {
        images[i] = new Image();
        images[i].src = "../leaf"+i+".gif";

    }

    addLeaf();
    setInterval(function () {addLeaf();}, intro_offset);
}

