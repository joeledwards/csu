function init()
{
    var form = document.forms[0];
    for (var idx in form.elements) {
        form.elements[idx].hFocused = false;
        addCookie(form.elements[idx]);
    }

    addHint(form.first_name,    "First",   -1);
    addHint(form.last_name,     "Last",    -1);
    addHint(form.company,       "Company", -1);
    addHint(form.email,         "your.email@provider.tld", -1);
    addHint(form.address,       "Address", -1);
    addHint(form.city,          "City",    -1);
    addHint(form.state,         "State",    2);
    addHint(form.zip,           "Zip",      5);

    addValidator(form.first_name,       "First Name",       validateNotEmpty);
    addValidator(form.last_name,        "Last Name",        validateNotEmpty);
    addValidator(form.company,          "Company",          validateNotEmpty);
    addValidator(form.email,            "E-mail Address",   validateNotEmpty);
    addValidator(form.phone_area,       "Phone Area Code",  validateInt);
    addValidator(form.phone_exchange,   "Phone Exchange",   validateInt);
    addValidator(form.phone_number,     "Phone Number",     validateInt);
    addValidator(form.address,          "Address",          validateNotEmpty);
    addValidator(form.city,             "City",             validateNotEmpty);
    addValidator(form.state,            "State",            validateNotEmpty);
    addValidator(form.zip,              "Zip",              validateInt);
    addValidator(form.pick_up,          "Pick-up Date",     validateInt);
    addValidator(form.drop_off,         "Drop-off Date",    validateInt);
}

function addCookie(input)
{
    if (input.type == "text") {
        textCookie(input);
    }
    else if (input.type == "radio") {
        radioCookie(input);
    }
}

function radioCookie(radio)
{
    radioFromCookie(radio);
    radio.onchange = function () {radioToCookie(radio);};
}

function radioToCookie(radio)
{
    if (radio.checked == true) {
        var time = new Date();
        time.setDate(time.getDate() + 1);
        setCookie(radio.name, radio.value, time);
    }
}

function radioFromCookie(radio)
{
    var cookieValue = getCookie(radio.name);
    if (cookieValue === undefined) {
        ;
    }
    else if (cookieValue == radio.value) {
        radio.checked = true;
    }
}

function textCookie(text)
{
    textFromCookie(text);
    text.onchange = function () {textToCookie(text);};
}

function textToCookie(text)
{
    var time = new Date();
    time.setDate(time.getDate() + 1);
    if (text.hHintText === undefined || text.hHintText != text.value) {
        setCookie(text.name, text.value, time);
    }
}

function textFromCookie(text)
{
    var cookieValue = getCookie(text.name);
    if (cookieValue === undefined) {
        ;
    }
    else {
        text.value = getCookie(text.name);
    }
}

function displayInfo()
{
    window.alert();
}

function getForm()
{
    return document.forms[0];
}

function register()
{
    var form = getForm();
    var expire = new Date();
    expire.setDate(expire.getDate() + 1);

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

var dateObject = new Date();
function displayCalendar(inputName, whichMonth) {
    var month = dateObject.getMonth();
    var monthArray = new Array("January" , "February" , "March"     ,
                               "April"   , "May"      , "June"      ,
                               "July"    , "August"   , "September" ,
                               "October" , "November" , "December"  );
    var dateToday = monthArray[month] + " " + dateObject.getDate() + ", " + dateObject.getFullYear();

    var calendarWin = window.open("", "CalWindow", "status=no,resizable=yes,width=400,height=320,left=200,top=200");
    calendarWin.focus();
    calendarWin.document.write("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' ");
    calendarWin.document.write("'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'>");
    calendarWin.document.write("<html xmlns='http://www.w3.org/1999/xhtml'>");
    calendarWin.document.write("<head><title>Central Valley Snowboarding</title>");
    calendarWin.document.write("<meta http-equiv='content-type' content='text/html; charset=iso-8859-1' />");
    calendarWin.document.write("<link rel='stylesheet' href='../../js_styles.css' type='text/css' /></head>");
    calendarWin.document.write("<body>");
    calendarWin.document.write("<table cellspacing='0' border='1' width='100%'>");

    dateObject.setMonth(dateObject.getMonth() + whichMonth);
    var month = dateObject.getMonth();

    calendarWin.document.write("<tr>");
    calendarWin.document.write("<td colspan='2'><a href='' onclick='self.opener.displayCalendar(\""+inputName+"\", -1);return false'>Previous</a></td>");
    calendarWin.document.write("<td colspan='3' align='center'><strong>"+monthArray[month]+" "+dateObject.getFullYear()+"</strong></td>");
    calendarWin.document.write("<td colspan='2' align='right'><a href='' onclick='self.opener.displayCalendar(\""+inputName+"\", +1);return false'>Next</a></td>");
    calendarWin.document.write("</tr>");

    calendarWin.document.write("<tr align='center'><td>Sun</td><td>Mon</td><td>Tue</td><td>Wed</td><td>Thu</td><td>Fri</td><td>Sat</td></tr>");
    calendarWin.document.write("<tr align='center'>");

    dateObject.setDate(1);
    var dayOfWeek = dateObject.getDay();
    for (var i=0; i<dayOfWeek; ++i) {
        calendarWin.document.write("<td>&nbsp;</td>");
    }

    var daysWithDates = 7 - dayOfWeek;
    var dateCounter = 1;

    for(var i=0; i<daysWithDates; ++i) {
        var curDate = monthArray[month] + " " + dateCounter + ", " + dateObject.getFullYear();
        calendarWin.document.write("<td><a href='' onclick='self.opener.document.forms[0]."+inputName+".value=\""+ curDate + "\";self.close(); return false'>" + dateCounter + "</a></td>");
        ++dateCounter;
    }

    var numDays = 0;
    // January, March, May, July, August, October, December
    switch (month) {
        case 1:
            var thisYear = dateObject.getYear();
            if (thisYear % 4 != 0) {
                numDays = 28;
            } else if (thisYear % 400 == 0) {
                numDays = 29;
            } else if (thisYear % 100 == 0) {
                numDays = 28;
            } else {
                numDays = 29;
            }
            break;
        case 3:
        case 5:
        case 8:
        case 10:
            numDays = 30;
            break;
        default:
            numDays = 31;
    }

    for (var rowCounter = 0; rowCounter < 5; ++rowCounter) {
        var weekDayCounter = 0;
        calendarWin.document.write("<tr align='center'>");
        while (weekDayCounter < 7) {
            var curDate = monthArray[month] + " " + dateCounter + ", " + dateObject.getFullYear();
            if (dateCounter <= numDays) {
                calendarWin.document.write("<td><a href='' onclick='self.opener.document.forms[0]."+inputName+".value=\"" + curDate + "\";self.close(); return false'>" + dateCounter + "</a></td>");
            }
            else {
                calendarWin.document.write("<td>&nbsp;</td>");
            }
            ++weekDayCounter;
            ++dateCounter;
        }
        calendarWin.document.write("</tr>");
    }
    calendarWin.document.write("</table></body></html>");
    calendarWin.document.close();
}
