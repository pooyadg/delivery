package com.example.delivery.controller

import java.time.ZonedDateTime

data class DeliveryPatchDto(
    var finishedAt: ZonedDateTime? = null,
    var status: String
)
