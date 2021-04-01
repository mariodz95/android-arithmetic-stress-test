package com.example.arithmeticstresstest.helper

import java.util.*

class DateHelper {

    fun getWeekStartDate(): Date? {
        val calendar = Calendar.getInstance()
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        return calendar.time
    }

    fun getWeekEndDate(): Date? {
        val calendar = Calendar.getInstance()
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        }
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
}