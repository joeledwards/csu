function toTitleCase()
{
    var form = document.forms[0];
    var text = form.candidate.value;
    var lower = text.toLowerCase();
    var words = lower.split(" ");
    var newWords = new Array();
    for (i in words) {
        var word = words[i];
        newWords[i] = word[0].toUpperCase() + word.substr(1);
    }
    var title = newWords.join(" ");
    form.candidate.value = title;
}
