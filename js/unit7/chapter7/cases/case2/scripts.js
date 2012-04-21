function checkPalindrome()
{
    var form = document.forms[0];
    var word = form.candidate.value;
    var lower = word.toLowerCase();
    var alnum = toAlnum(lower);
    var perfectCandidate = false;
    if (alnum == lower) {
        perfectCandidate = true;
    }

    if (alnum.length == 0) {
        window.alert("No alphanumeric characters entered");
    }
    else if (alnum == reverse(alnum)) {
        if (perfectCandidate) {
            window.alert("'" +word+"' is a perfect palindrome." );
        }
        else {
            window.alert("'" +word+"' is a standard palindrome." );
        }
    }
    else {
        window.alert("'" +word+"' is not a palindrome." );
    }
}

function reverse(word) {
    return word.split("").reverse().join("");
}

function toAlnum(word)
{
    var pattern = /[0-9a-zA-Z]/;
    var alnum = "";
    for (i in word) {
        var c = word[i];
        if (pattern.test(c)) {
            alnum += c;
        }
    }
    return alnum;
}

