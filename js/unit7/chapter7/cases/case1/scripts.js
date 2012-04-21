var secret = "administration";

function init()
{
    var form = document.forms[0];
    var startGuess = "";
    for (i in secret) {
        startGuess += "*";
    }
    form.guessed.value = startGuess; 
}

function applyGuess()
{
    var form = document.forms[0];
    var guess = form.guess.value[0];
    var guessed = form.guessed.value;
    var newGuessed = "";
    for (i in secret)
    {
        newGuessed += (guess == secret[i]) ? secret[i] : guessed[i];
    }
    form.guessed.value = newGuessed;
    form.guess.value = "";
}

