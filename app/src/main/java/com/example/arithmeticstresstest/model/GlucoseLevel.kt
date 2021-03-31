package com.example.arithmeticstresstest.model

import java.io.Serializable
import java.util.*

data class GlucoseLevel(
        var glucoseLevel: Float? = null,
        var glucoseType: String? = null,
        var date: Date? = null,
) : Serializable
