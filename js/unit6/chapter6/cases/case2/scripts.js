function showTime() {
    var ts = new Date();
    var hour = ts.getHours();
    var minute = ts.getMinutes();
    var second = ts.getSeconds();
    var indicator = (hour < 12) ? "AM" : "PM";

    if (minute < 10) {
        minute = "0" + minute;
    }

    hour   = (hour   > 12) ? (hour - 12)  : hour;
    minute = (minute < 10) ? "0" + minute : minute;
    second = (second < 10) ? "0" + second : second;

    var timeString = hour+ ":" +minute+ ":" +second+ " " +indicator;

    document.forms[0].time.value = timeString;
}

function addHint(input, hintText, maxLength) {
    input.hHintText = hintText;
    input.hMaxLength = maxLength;
    input.hFocused = false;
    input.onfocus = function () {hideHint(this);}
    input.onblur = function () {showHint(this);}
    hint(input); 
}

function showHint(input) {
    input.hFocused = false;
    hint(input);
}

function hideHint(input) {
    input.hFocused = true;
    hint(input);
}

function hint(input) {
    var maxLength = input.hMaxLength;
    var hintText = input.hHintText;
    var show = !input.hFocused;

    if (show) {
        if (maxLength > 0) {
            input.maxLength = hintText.length;
        }
        if (input.value == "") {
            input.style.color = "gray";
            input.value = hintText;
        }
        else if (input.value == hintText) {
            input.style.color = "gray";
        }
        else {
            input.style.color = "";
        }
    }
    else {
        if (input.value == hintText) {
            input.value = "";
        }
        input.style.color = "";
        if (maxLength > 0) {
            input.maxLength = maxLength;
        }
    }
}

function shipSame(checkbox) {
    var form = document.forms[0];
    if (checkbox.checked) {
        form.ship_address.value = form.bill_address.value;
        form.ship_city.value    = form.bill_city.value;
        form.ship_state.value   = form.bill_state.value;
        form.ship_zip.value     = form.bill_zip.value;
    } else {
        form.ship_address.value = "";
        form.ship_city.value    = "";
        form.ship_state.value   = "";
        form.ship_zip.value     = "";
    }
    hint(form.ship_address);
    hint(form.ship_city);
    hint(form.ship_state);
    hint(form.ship_zip);
}

function init() {
    var form = document.forms[0];
    for (var idx in form.elements) {
        form.elements[idx].hFocused = false;
    }
    addHint(form.first_name, "First", -1);
    addHint(form.last_name, "Last", -1);
    addHint(form.bill_address, "Street", -1);
    addHint(form.bill_city, "City", -1);
    addHint(form.bill_state, "State", 2);
    addHint(form.bill_zip, "Zip", 5);
    addHint(form.ship_address, "Street", -1);
    addHint(form.ship_city, "City", -1);
    addHint(form.ship_state, "State", 2);
    addHint(form.ship_zip, "Zip", 5);
}

function preProcess(form) {
    ;
}
