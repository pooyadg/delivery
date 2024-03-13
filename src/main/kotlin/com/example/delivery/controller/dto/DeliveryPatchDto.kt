package com.example.delivery.controller.dto

import java.time.ZonedDateTime

data class DeliveryPatchDto(
    val id: String? = null,
    var finishedAt: ZonedDateTime? = null,
    var status: String
)
