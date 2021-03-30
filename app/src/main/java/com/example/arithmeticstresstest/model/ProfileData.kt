package com.example.arithmeticstresstest.model

import java.io.Serializable
import java.util.*

data class ProfileData(
    var name: String?= null,
    var lastName: String? = null,
    var typeOfDiabetes: String? = null,
    var weight: Float? = null,
    var dateOfBirth: Date? = null) : Serializable

