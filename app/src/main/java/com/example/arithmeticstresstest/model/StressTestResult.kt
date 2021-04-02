package com.example.arithmeticstresstest.model

import java.io.Serializable
import java.util.*

data class StressTestResult(
        var userEmail: String? = null,
        var testScore: Int? = null,
        var testQuestions: Int? = null,
        var dateInserted: Date? = null
): Serializable

