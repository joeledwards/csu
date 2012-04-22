var NONE = 0;
var ALPHA = 1;
var NUMERIC = 2;
var type = NONE;

function questionSelected(radio)
{
    document.forms[0].answer.value = "";
    try {
        if (radio.value.split('-')[1] == "alpha") {
            type = ALPHA;
        }
        else if (radio.value.split('-')[1] == "numeric") {
            type = NUMERIC;
        }
        else {
            type = NONE;
        }
    }
    catch (e) {
        type = NONE;
    }
}

function verifyKey(input, keyEvent)
{
    if (type == NONE) {
        window.alert("You must select a question first.");
        return false;
    }

    try {
        var keyCode = keyEvent.which;
    }
    catch (e) {
        var keyCode = keyEvent.keyCode;
    }
    var character = String.fromCharCode(keyCode);

    var pattern = /[a-zA-Z ]/;
    if (type == NUMERIC) {
        pattern = /\d/;
    }

    var result = true;
    try {
        if (!pattern.test(character)) {
            if (type == NUMERIC) {
                throw "You must enter only numeric characters.";
            }
            else {
                throw "You must enter only alphabetic characters.";
            }
        }
    }
    catch (e) {
        window.alert(e);
        result = false;
    }
    return result;
}

