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
