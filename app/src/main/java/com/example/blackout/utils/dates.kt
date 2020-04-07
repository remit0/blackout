package com.example.blackout.utils

import java.util.*


fun getCurrentWeek(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.WEEK_OF_YEAR)
}


fun getLastWeek(): Int {
    val currentWeek = getCurrentWeek()
    return if (currentWeek > 1) currentWeek - 1 else 52
}


fun getWeekStartMilli(week: Int): Long {

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.clear(Calendar.MINUTE)
    calendar.clear(Calendar.SECOND)
    calendar.clear(Calendar.MILLISECOND)
    calendar.set(Calendar.WEEK_OF_YEAR, week)
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    return calendar.timeInMillis

}