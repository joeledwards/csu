
function basename(uri)
{
    var parts = uri.split('/');
    return parts[parts.length - 1];
}

function toggleImage(id)
{
    var image = document.getElementById(id+"-img");
    var link  = document.getElementById(id+"-a");

    if (basename(image.src) == "cottage_small.jpg") {
        image.src = "../cottage_large.jpg";
        link.innerHTML = "View smaller image";
    }
    else {
        image.src = "../cottage_small.jpg";
        link.innerHTML = "View larger image";
    }
}

