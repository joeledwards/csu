var max_id = 0;

function addBowler()
{
    var name = document.getElementById('name-entry').value;
    if (name.length < 1) {
        return;
    }
    var table = document.getElementById('bowler-table');
    var empty = document.getElementById('empty');
    if (empty != undefined) {
        for (i in table.rows) {
            if (i > 0) {
                table.deleteRow(i);
            }
        }
    }
    max_id++;
    var id = max_id;
    var index = table.rows.length;

    table.insertRow(-1);
    var row = table.rows[index];
    row.setAttribute("id", "bowler-" +id+ "-row");
    row.innerHTML = "<td>" +name+ "</td><td><button onclick=\"removeBowler('"+id+"'); return false;\">Remove</button> <input name=\"bowler-"+id+"\" type=\"hidden\" value=\"" +name+ "\"</td>";
}

function removeBowler(id)
{
    var table = document.getElementById('bowler-table');
    var id = "bowler-" + id+ "-row";
    for (var i = 1; i < table.rows.length; i++) {
        row = table.rows[i];
        if (row.getAttribute("id") == id) {
            table.deleteRow(i);
            break;
        }
    }
}
