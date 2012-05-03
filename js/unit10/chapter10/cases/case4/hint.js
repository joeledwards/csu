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
    var form = checkbox.form;
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


function readyDate(delay) {
    var form = document.forms[0];
    var ts = new Date();
    var mDay = ts.getDate() + delay;
    if (ts.getHours() >= 15) {
        mDay++;
    }
    ts.setDate(mDay);

    var wDay = ts.getDay();
    if (wDay == 5) {
        mDay += 2;
    }
    else if (wDay == 6) {
        mDay++;
    }
    ts.setDate(mDay);
    form.ready.value = dayArray[ts.getDay()]+ ", " +monthArray[ts.getMonth()]+ " " +ts.getDate();
}

function addValidator(input, title, func) {
    input.mValidate = function (el, str, fn) {
        //window.alert("name="+input.name+"title="+title+"func="+func);
        var e = el;
        var s = str;
        var f = fn;
        return function () {
            return f(e,s);
        };
    }(input, title, func);
}

function isDigits(value){ 
    for (i = 0 ; i < value.length ; i++) { 
        if ((value.charAt(i) < '0') || (value.charAt(i) > '9')) {
            return false 
        }
    } 
    return true; 
}

function validateNotEmpty(input, name) {
    if (input.value == "") {
        window.alert(name+" must contain a value.");
        input.focus();
        return false;
    }
    return true;
}

function validateFloat(input, name) {
    if ((input.value == "") || (isNaN(input.value) == true)) {
        window.alert(name+" must be a numeric value.");
        input.focus();
        return false;
    }
    return true;
}

function validateInt(input, name) {
    if ((input.value == "") || (isDigits(input.value) == false)) {
        window.alert(name+" must be an integer value (digits only)");
        input.focus();
        return false;
    }
    return true;
}

function preProcess(form) {
    for (var idx in form.elements) {
        var el = form.elements[idx];
        if (el.hHintText == el.value) {
            el.value = "";
        }
        //window.alert("validate method for input."+el.name+" = "+el.mValidate);
        if (el.mValidate) {
            if (el.mValidate() == false) {
                return false;
            }
        }
    }
    return true;
}
