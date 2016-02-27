console.log("APP STARTED");

var v = 20; // per day

console.log("countDaily(24) = " + countDaily(24));
console.log("countWeekly(1, 24) = " + countWeekly(1, 24));
console.log("countMonthly(1, 24) = " + countMonthly(1, 24));
console.log("EXPECTING " + v + " FOR ALL");

function countDaily(hourOfDay) {
    return hourOfDay / 24 * v;
}

function countWeekly(dayOfWeek, hourOfDay) {
    return (dayOfWeek - 1 + hourOfDay / 24) * v;
}

function countMonthly(dayOfMonth, hourOfDay) {
    return (dayOfMonth - 1 + hourOfDay / 24) * v;
}
