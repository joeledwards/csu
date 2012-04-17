var dayArray   = new Array("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
var monthArray = new Array("January" , "February" , "March"     ,
                           "April"   , "May"      , "June"      ,
                           "July"    , "August"   , "September" ,
                           "October" , "November" , "December"  );
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

function init() {
    var form = document.forms[0];
    for (var idx in form.elements) {
        form.elements[idx].hFocused = false;
    }
    addHint(form.first_name,    "First",   -1);
    addHint(form.last_name,     "Last",    -1);
    addHint(form.email,         "your.email@provider.tld", -1);
    addHint(form.bill_address,  "Address", -1);
    addHint(form.bill_city,     "City",    -1);
    addHint(form.bill_state,    "State",    2);
    addHint(form.bill_zip,      "Zip",      5);
    addHint(form.ship_address,  "Address", -1);
    addHint(form.ship_city,     "City",    -1);
    addHint(form.ship_state,    "State",    2);
    addHint(form.ship_zip,      "Zip",      5);

    addValidator(form.first_name,    "First Name",       validateNotEmpty);
    addValidator(form.last_name,     "Last Name",        validateNotEmpty);
    addValidator(form.email,         "E-mail Address",   validateNotEmpty);
    addValidator(form.phone_area,    "Phone Area Code",  validateInt);
    addValidator(form.phone_exchange,"Phone Exchange",   validateInt);
    addValidator(form.phone_number,  "Phone Number",     validateInt);
    addValidator(form.bill_address,  "Billing Address",  validateNotEmpty);
    addValidator(form.bill_city,     "Billing City",     validateNotEmpty);
    addValidator(form.bill_state,    "Billing State",    validateNotEmpty);
    addValidator(form.bill_zip,      "Billing Zip",      validateInt);
    addValidator(form.ship_address,  "Shipping Address", validateNotEmpty);
    addValidator(form.ship_city,     "Shipping City",    validateNotEmpty);
    addValidator(form.ship_state,    "Shipping State",   validateNotEmpty);
    addValidator(form.ship_zip,      "Shipping Zip",     validateInt);

    var ts = new Date();
    form.order_time.value = ts.getFullYear()+"-"+(ts.getMonth()+1)+"-"+ts.getDate()+" "+ts.getHours()+":"+ts.getMinutes()+":"+ts.getSeconds();
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
