var runner = undefined;
var frames = new Array();
var frame = 0;

function init()
{
    for (var i = 0; i < 6; i++) {
        frames[i] = new Image();
        frames[i].src = "../puppy"+i+".gif";
    }

    runner = document.getElementById("runner");

    setInterval('updateFrame()', 100);
}

function updateFrame()
{
    frame++;
    if (frame >= frames.length) {
        frame = 0;
    }
    runner.src = frames[frame].src;
}

