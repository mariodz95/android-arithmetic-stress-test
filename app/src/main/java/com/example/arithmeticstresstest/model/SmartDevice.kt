package com.example.arithmeticstresstest.model

import java.util.*

data class SmartDevice(
        var calories: Int? = null,
        var steps: Int? = null,
        var distance: Int? = null,
        var heartRate: Int? = null,
        var dateTime: Date? = null,
        var durationTimeMinutes: Int? = null
)
