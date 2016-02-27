console.log("APP STARTED");

var v = 20; // per day
var started = new Date("2016-02-18T10:00");
var now = new Date();
var daysElapsed = (now.getTime() - started.getTime()) / 1000 / 60 / 60 / 24;

console.log("daily: " + countDaily(now, daysElapsed));
console.log("weekly: " + countWeekly(now, daysElapsed));
console.log("monthly: " + countMonthly(now, daysElapsed));

function countDaily(now, daysElapsed) {
    if (daysElapsed < 0) return 0;
    if (daysElapsed < 1) return daysElapsed * v;
    else return hourOfDay(now) / 24 * v;
}

function countWeekly(now, daysElapsed) {
    if (daysElapsed < 0) return 0;
    if (daysElapsed < dayOfWeek(now)) return daysElapsed * v;
    else return (dayOfWeek(now) - 1 + hourOfDay(now) / 24) * v;
}

function countMonthly(now, daysElapsed) {
    if (daysElapsed < 0) return 0;
    if (daysElapsed < dayOfMonth(now)) return daysElapsed * v;
    else return (dayOfMonth(now) - 1 + hourOfDay(now) / 24) * v;
}

function hourOfDay(now) {
    return now.getHours() + now.getMinutes() / 60 + now.getSeconds() / 3600;
}

function dayOfWeek(now) {
    return now.getDay();
}

function dayOfMonth(now) {
    return now.getDate();
}
