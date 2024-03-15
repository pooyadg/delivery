package com.example.delivery.controller.dto

data class DeliverySummaryDto(
    val deliveries: Int = 0,
    val averageMinutesBetweenDeliveryStart: Int = -1
)
