package com.example.delivery.controller

import java.time.ZonedDateTime

data class DeliveryDto(
    val id: String? = null,
    val vehicleId: String,
    val startedAt: ZonedDateTime,
    val finishedAt: ZonedDateTime? = null,
    val status: String
)
