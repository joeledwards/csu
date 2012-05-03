
function add(a, b)
{
    return parseInt(a) + parseInt(b);
}

function mul(a, b)
{
    return parseInt(a) * parseInt(b);
}

function showATable()
{
    showTable(add);
}

function showMTable()
{
    showTable(mul);
}

function showTable(mathFunc) {

    var mathWin = window.open("", "MathWindow", "status=no,resizable=yes,width=500,height=500,left=200,top=200");
    mathWin.focus();
    mathWin.document.write("<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Strict//EN' ");
    mathWin.document.write("'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'>");
    mathWin.document.write("<html xmlns='http://www.w3.org/1999/xhtml'>");
    mathWin.document.write("<head><title>Central Valley Snowboarding</title>");
    mathWin.document.write("<meta http-equiv='content-type' content='text/html; charset=iso-8859-1' />");
    mathWin.document.write("<link rel='stylesheet' href='../../js_styles.css' type='text/css' /></head>");
    mathWin.document.write("<body>");
    mathWin.document.write("<table cellspacing='0' border='1' width='100%'>");

    for (var i = 0; i <= 10; i++) {
        mathWin.document.write("<tr>");
        for (var j = 0; j <= 10; j++) {
            if ((i == 0) || (j == 0)) {
                mathWin.document.write("<th>");
                if (i != 0) {
                    mathWin.document.write(i);
                }
                else if (j != 0) {
                    mathWin.document.write(j);
                }
                mathWin.document.write("</th>");
            }
            else {
                mathWin.document.write("<td>" +mathFunc(i, j)+ "</td>");
            }
        }
        mathWin.document.write("</tr>");
    }

    mathWin.document.write("</table>");
    mathWin.document.write("<input type='button' value='Close' onclick='window.close();'>");
    mathWin.document.write("</body></html>");
    mathWin.document.close();
}
