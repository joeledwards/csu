
function getForm()
{
    return document.forms[0];
}

function register()
{
    var form = getForm();
    var expire = new Date();
    expire.setFullYear(expire.getFullYear() + 1);

    if (form.name.value == getCookie("name")) {
        var confirmation = window.confirm("Our records indicate that you are already registered. Do you wish to register again?");
        if (!confirmation) {
            return false;
        }
    }

    setCookie("name", form.name.value, expire);
    setCookie("company", form.company.value, expire);
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

