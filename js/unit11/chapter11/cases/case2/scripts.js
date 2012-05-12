var image;
var interval = 100;
var intro_offset = 217;
var maxId = 0;
var maxBugs = 7;
var bugInterval;

var height;
var width;

function updateFrame(context)
{
    if (context._y_right) { 
        context._y_pos += context._y_rate;
        if (context._y_pos >= width) {
            context._y_pos = width;
            context._y_right = false;
        }
    }
    else {
        context._y_pos -= context._y_rate;
        if (context._y_pos <= 0) {
            context._y_pos = 0;
            context._y_right = true;
        }
    }

    if (context._x_down) { 
        context._x_pos += context._x_rate;
        if (context._x_pos >= height) {
            context._x_pos = height;
            context._x_down = false;
        }
    }
    else {
        context._x_pos -= context._x_rate;
        if (context._x_pos <= 0) {
            context._x_pos = 0;
            context._x_down = true;
        }
    }

    document.getElementById("bug-"+context._id+"-div").style.top = context._x_pos+"px";
    document.getElementById("bug-"+context._id+"-div").style.left = context._y_pos+"px";
}

function addBug()
{
    if (maxId == maxBugs) {
        clearInterval(bugInterval);
        return;
    }
    var context = new Object;
    context._x_rate = Math.floor(Math.random() * 15) + 10; // 5 - 20 pixels per 
    context._y_rate = Math.floor(Math.random() * 15) + 10; // 5 - 20 pixels per 
    context._x_right = true;
    context._y_down = false;

    context._y_pos = -50;
    context._x_pos = Math.floor(Math.random() * (height - 50)) + 25;
    context._id = maxId++;

    document.write("<div id=\"bug-"+context._id+"-div\" style=\"position:absolute; left:"+context._x_pos+"px; top:"+context._y_pos+"px\">");
    document.write("<img id=\"bug-"+context._id+"-img\" src=\"\" />");
    document.write("</div>");

    document.getElementById("bug-"+context._id+"-img").src = image.src;

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
        width = window.innerWidth;
    }
    image = new Image();
    image.src = "../bug.gif";

    addBug();
    bugInterval = setInterval(function () {addBug();}, intro_offset);
}

