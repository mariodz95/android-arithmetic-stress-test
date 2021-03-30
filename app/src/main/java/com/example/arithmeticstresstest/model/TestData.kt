package com.example.arithmeticstresstest.model

import java.io.Serializable
import java.util.*

data class TestData (
        var glucoseLevelBeforeTest: Float? = null,
        var glucoseLevelAfterTest: Float? = null,
        var heartRateBeforeTest: Int? = null,
        var heartRateAfterTest: Int? = null,
        var stressLevelBeforeTest: Int? = null,
        var stressLevelAfterTest: Int? = null,
        var insertedDate: Date? = null,
        var testResult: Int? = null,
        var testQuestions: Int? = null,
        var testDuration: String? = null,
        var testWeight: String? = null,
        var userId: String? = null) : Serializable


