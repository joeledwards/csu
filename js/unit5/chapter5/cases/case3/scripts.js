var formIndex = 0;
var name_hint = "First and Last Name";
var email_hint = "your.email@provider.tld";

function getForm() {
    return document.forms[formIndex];
}

function init() {
    hintName(true);
    hintEmail(true);
}

function hintName(show) {
    var form = getForm();
    return hint(form.name, name_hint, show);
}

function hintEmail(show) {
    var form = getForm();
    return hint(form.email, email_hint, show);
}

function hint(input, hint_text, show) {
    if (show) {
        if (input.value == "") {
            input.value = hint_text;
            input.style.color = "gray";
        }
    } else {
        if (input.value == hint_text) {
            input.value = "";
            input.style.color = "";
        }
    }
}

function isDigits(value){ 
    for (i = 0 ; i < value.length ; i++) { 
        if ((value.charAt(i) < '0') || (value.charAt(i) > '9')) {
            return false 
        }
    } 
    return true; 
}

function isInt(value) { 
    if ((parseFloat(value) == parseInt(value)) && !isNaN(value)) {
        return true;
    } 
    return false;
}

function keyDigitOnly(input, evt) {
    var stop = false;
    var code = evt.which;
    if (code == 13) {
        return true;
    }

    var value = String.fromCharCode(code);
    switch (value) {
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            return true;
    }
    return false;
}

function verifyPhonePart(input) {
    if ((input.value.length < input.maxLength) || !isDigits(input.value)) {
        return false;
    }
    return true;
}

function validatePassword() {
    var form = getForm();
    if (form.password_confirm.value == form.password.value) {
        form.password_confirm.style.backgroundColor = "";
    } else {
        form.password_confirm.style.backgroundbackgroundColor= "#FF8888";
    }
    return true;
}

function confirmSubmit() {
    var form = getForm();

    if ((form.name.value == name_hint) || (form.name.value == "")) {
        window.alert("Invalid Entry: name");
        form.name.focus()
        return false;
    }
    if ((form.email.value == email_hint) || (form.email.value == "")) {
        window.alert("Invalid Entry: email");
        form.email.focus()
        return false;
    }

    if (!verifyPhonePart(form.phone_area)) {
        window.alert("Invalid Entry: phone area code");
        form.phone_area.focus()
        return false;
    }
    if (!verifyPhonePart(form.phone_exchange)) {
        window.alert("Invalid Entry: phone exchange");
        form.phone_exchange.focus()
        return false;
    }
    if (!verifyPhonePart(form.phone_number)) {
        window.alert("Invalid Entry: phone number");
        form.phone_number.focus()
        return false;
    }

    if (form.password.value == "") {
        window.alert("Invalid Entry: password is empty");
        form.password.focus()
        return false;
    }
    if (form.password.value != form.password_confirm.value) {
        window.alert("Invalid Entry: passwords do not match");
        form.password.focus()
        return false;
    }

    if (form.security_answer.value == "") {
        window.alert("Invalid Entry: you must enter an answer for the security question");
        form.security_answer.focus()
        return false;
    }

    var none = true;
    for (var j in form.special_offers) {
        if (form.special_offers[j].checked) {
            none = false;
            break;
        }
    }
    if (none) {
        window.alert("Invalid Entry: you must either accept or decline special offers");
        form.special_offers[0].focus()
        return false;
    }

    none = true;
    for (var k in form.interests) {
        if (form.interests[k].checked) {
            none = false;
            break;
        }
    }
    if (none) {
        window.alert("Invalid Entry: you must select at least one interest");
        form.interests[0].focus()
        return false;
    }

    return true;
}

function confirmReset() {
    return window.confirm("Are you sure you want to reset the form?");
}



