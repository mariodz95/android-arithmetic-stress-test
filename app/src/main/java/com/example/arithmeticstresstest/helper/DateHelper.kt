package com.example.arithmeticstresstest.helper

import java.util.*

class DateHelper {

    fun getWeekStartDate(): Date? {
        val calendar = Calendar.getInstance()
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }
        return calendar.time
    }

    fun getWeekEndDate(): Date? {
        val calendar = Calendar.getInstance()
        while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, 1)
        }
        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
}