var interval = 20;
var animationDuration = 2000;
var height;
var width;
var curtainInterval;

function slideCurtain(context)
{
    var curtain = document.getElementById("curtain");
    context._width -= context._x_rate;
    if (context._width > 0) {
        curtain.style.width = context._width+"px";
    }
    else {
        curtain.style.height = 0;
        curtain.style.width = 0;
        curtain.style.hidden = true;
        curtain.style.zIndex = 0;
        clearInterval(context._inerval);
    }
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

    document.getElementById("store").innerHTML = "<div id=\"curtain\" style=\"position:absolute; left:0px; top:0px; z-index:1000; width:"+width+"px; height:"+height+"px; background:black;\"></div>";

    var context = new Object;
    var steps = animationDuration / interval;
    context._x_rate = width / steps;
    context._width = width;
    context._interval = setInterval(function () {
        slideCurtain(context);
    }, interval);
}

