var unit = 1;
var init = false;
// PROJECTS
var projects = [
    [1, 1, ""],
];

// CASES
var cases = [
    [1, 1, ""],
];
var types = [
    "projects",
    "cases"
]


function printSummary() {
    summarize("project", projects);
    summarize("case", cases);
}

function summarize(var prefix, var series) {
    for (var i in series) {
        var info = series[i]; 
        var chapter = info[0];
        var exercise = info[1];
        var file_name = info[2];
        var url = "chapter" +chapter+ "/" +prefix+ "s/" +prefix+exercise+ "/" +file_name;

        var new_content = ""
        new_content += "<tr>";

        // Identifier
        new_content += "<td>";
        new_content += chapter+ "-" +exercise;
        new_content += "</td>";

        // URL
        new_content += "<td>";
        new_content += "";
        new_content += "</td>";

        new_content += "</tr>";

        $("#"+table+"s").append(new_content);
    }
}
/*
    document.write("<table><colgroup span=\"3\" width=\"20%\"");
    document.write("<tr><th>Name</th><th>Location</th><th>Height (in meters)</th></tr>");
    document.write("<tr><td>Angel (upper fall)</td><td>Venezuela</td><td>807</td></tr>");
    document.write("<tr><td>Itatinga</td><td>Brazil</td><td>628</td></tr>");
    document.write("<tr><td>Cuquenan</td><td>Guyana/Venezuela</td><td>610</td></tr>");
    document.write("<tr><td>Ormeli</td><td>Norway</td><td>563</td></tr>");
    document.write("<tr><td>Tysse</td><td>Norway</td><td>533</td></tr>");
    document.write("<tr><td>Pilao</td><td>Brazil</td><td>524</td></tr>");
    document.write("<tr><td>Ribbon</td><td>USA</td><td>491</td></tr>");
    document.write("<tr><td>Vestre Mardola</td><td>Norway</td><td>468</td></tr>");
    document.write("<tr><td>Kaieteur</td><td>Guyana</td><td>457</td></tr>");
    document.write("<tr><td>Cleve-Garth</td><td>New Zealand</td><td>450</td></tr>");
    document.write("</table>");
    */
