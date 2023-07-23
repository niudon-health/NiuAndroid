package com.niudon.swasth.domain.models

import com.google.firebase.Timestamp
import java.util.*

data class NDWeightLog(
    val id: String = UUID.randomUUID().toString(),
    val weight: Double = 0.0,
    val epoch : Long?= null
    //var createdOn: Date? = null,
    //var updatedOn: Date? = null,
    //val isDeleted: Boolean = false
)
