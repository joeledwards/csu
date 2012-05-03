var registered;
var nag;
var name;
var email;

function getForm()
{
    return document.forms[0];
}

function init()
{
    var expire = new Date();
    expire.setFullYear(expire.getFullYear() + 1);

    if (getCookie("registered") != "TRUE") {
        registered = false;
        if (getCookie("nag") === undefined) {
            nag = 0;
        }
        else {
            nag = parseInt(getCookie("nag"));
            if ((nag % 3) == 0) {
                window.alert("Don't forget to register");
            }
            nag++;
        }
        setCookie("nag", ""+nag, expire);
    }
    else {
        var form = getForm();
        var name = getCookie("name");
        var email = getCookie("email");
        window.alert("Hello, " + name + "! You are registered under the e-mail address " + email);
        form.name.value = name;
        form.email.value = email;
    }
}

function register()
{
    var form = getForm();
    var expire = new Date();
    expire.setFullYear(expire.getFullYear() + 1);
    setCookie("registered", "TRUE", expire);
    setCookie("name", form.name.value, expire);
    setCookie("email", form.email.value, expire);
    return true;
}

function deleteCookie(name)
{
    var time = new Date();
    time.setFullYear(time.getFullYear() - 1);
    setCookie(name, "DELETE", time);
}

function setCookie(name, value, expire)
{
    document.cookie = name + "=" + encodeURIComponent(value) + ";expires=" + expire.toUTCString();
}

function getCookie(name)
{
    var cookies = getCookies();
    return cookies[name];
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

