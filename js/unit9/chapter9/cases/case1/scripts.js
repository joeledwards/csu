function init()
{
    var cookies = getCookies();
    var now = new Date();
    now.setFullYear(now.getFullYear() + 1);
    var count;
    if (cookies["revisit"] === undefined || cookies["count"] === undefined) {
        document.cookie = "revisit=" + encodeURIComponent("TRUE") + ";expires=" + now.toUTCString();
        document.cookie = "count=" + encodeURIComponent("1") + ";expires=" + now.toUTCString();
        document.write("<h2>Welcome to the Counter Web Site!</h2>");
        document.write("<h3>Be sure to bookmark the site so you can visit us again.</h3>");
    } else {
        count = parseInt(cookies["count"]);
        count++;

        document.write("<h2>Cookies:</h2>");
        document.write("<p>");
        for (name in cookies) {
            document.write(name+ " = " +cookies[name]+ " <br />");
        }
        document.write("</p>");

        document.cookie = "count=" + encodeURIComponent(""+count) + ";expires=" + now.toUTCString();
    }
}

function getCookies()
{
    var cookieData = decodeURIComponent(document.cookie);
    var cookiesCombined = cookieData.split("; ");
    var cookies = new Array();
    for (i in cookiesCombined) {
        var parts = cookiesCombined[i].split("=");
        var name  = parts[0];
        var value = parts[1];
        cookies[name] = value;
    }
    return cookies;
}

