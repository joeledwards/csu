function getDisplayFrame()
{
    return parent.display_frame;
}

function writeDocument(country, info)
{
    frame = getDisplayFrame();
    //frame.location.href = "";
    //frame.location.href = "blank.html";
    frame.document.getElementsByTagName("body")[0].innerHTML = "";
    frame.document.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
    frame.document.write("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
    frame.document.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    frame.document.write("<head>");
    frame.document.write("    <title>Country Info</title>");
    frame.document.write("    <meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\" />");
    frame.document.write("    <link rel=\"stylesheet\" href=\"../../js_styles.css\" type=\"text/css\" />");
    frame.document.write("</head>");
    frame.document.write("<body>");
    frame.document.write("<table>");
    frame.document.write("<tr><th colspan=\"2\">"+country+"</th></tr>");
    for (var title in info) {
        frame.document.write("<tr><td>"+title+"</td><td>"+info[title]+"</td></td>");
    }
    frame.document.write("</table>");
    frame.document.write("</body>");
    frame.document.write("</html>");
}

function displayAustralia()
{
    var country = "Commonwealth of Australia";
    var info = {
        "Capital"               : "Canberra",
        "Official language(s)"  : "None",
        "Government"            : "Federal parliamentray constitutional monarchy",
        "Area"                  : "2,941,299 sq mi",
        "Population"            : "22,888,891 (52nd, 2011 estimate)",
        "GDP (PPP)"             : "$914.482 billion (18th, 2011 estimate)",
        "Per capita (PPP)"      : "$40,234 (12th, 2011 estimate)"
    };
    writeDocument(country, info);
}

function displayBrazil()
{
    var country = "Federative Republic of Brazil";
    var info = {
        "Capital"               : "Brasilia",
        "Official language(s)"  : "Portugese",
        "Government"            : "Federal presedential constitutional republic",
        "Area"                  : "3,287,597 sq mi (5th)",
        "Population"            : "192,376,496 (5th, 2011 estimate)",
        "GDP (PPP)"             : "$2.294 billion (7th, 2011 estimate)",
        "Per capita (PPP)"      : "$11,769 (75th, 2011 estimate)"
    };
    writeDocument(country, info);
}

function displayGermany()
{
    var country = "Federal Republic of Germany";
    var info = {
        "Capital"               : "Berlin",
        "Official language(s)"  : "German",
        "Government"            : "Federal parliamentary constitutional republic",
        "Area"                  : "137,847 sq mi (63rd)",
        "Population"            : "81,799,600 (16th, 2010 estimate)",
        "GDP (PPP)"             : "3.099 trillian (5th, 2011 estimate)",
        "Per capita (PPP)"      : "$37,896 (18th, 2011 estimate)"
    };
    writeDocument(country, info);
}

function displayJapan()
{
    var country = "Japan";
    var info = {
        "Capital"               : "Tokyo",
        "Official language(s)"  : "None",
        "Government"            : "Unitary parliamentary democracy and constitutional monarchy",
        "Area"                  : "145,925 sq mi (62nd)",
        "Population"            : "127,799,000 (10th, 2011 estimate)",
        "GDP (PPP)"             : "$4.440 trillian (4th, 2011 estimate)",
        "Per capita (PPP)"      : "$34,739 (25th, 2011 estimate)"
    };
    writeDocument(country, info);
}

function displayMexico()
{
    var country = "United Mexican States";
    var info = {
        "Capital"               : "Mexico City",
        "Official language(s)"  : "None",
        "Government"            : "Fedeal presidential constitutional republic",
        "Area"                  : "761,606 sq mi (14th)",
        "Population"            : "112,322,757 (11th, 2010 census)",
        "GDP"                   : "$1.661 trillion",
        "Per capita (PPP)"      : "$14,609"
    };
    writeDocument(country, info);
}

function displayUSA()
{
    var country = "United States of America";
    var info = {
        "Capital"               : "Washington, D.C.",
        "Official language(s)"  : "None at federal level",
        "Government"            : "Federal presidential constitutional repubic",
        "Area"                  : "3,794,101 sq mi (3rd/4th)",
        "Population"            : "313,436,000 (3rd, 2012 estimate)",
        "GDP"                   : "$15.094 trillion (1st)",
        "Per capita (PPP)"      : "$48,386 (8th)"
    };
    writeDocument(country, info);
}
