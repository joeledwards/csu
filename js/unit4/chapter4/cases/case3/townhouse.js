var images = [
    "townhouse.jpg",
    "townhouse2.jpg",
    "townhouse3.jpg",
    "townhouse4.jpg",
    "townhouse5.jpg",
];
var index = 0;

function nextImage() {
    var img = document.images[0];
    index++;
    if (index >= images.length) {
        index = 0;
    }
    img.src = images[index];
}

function previousImage() {
    var img = document.images[0];
    index--;
    if (index < 0) {
        index = images.length - 1;
    }
    img.src = images[index];
}

function previousMouse(hover) {
    var img = document.images[1];
    if (hover) {
        img.src = "previous_red.gif";
    }
    else {
        img.src = "previous_blue.gif";
    }
}

function nextMouse(hover) {
    var img = document.images[2];
    if (hover) {
        img.src = "next_red.gif";
    }
    else {
        img.src = "next_blue.gif";
    }
}

