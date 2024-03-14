package com.example.delivery.service.mapper

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.controller.dto.DeliverySummaryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliveryStatus
import com.example.delivery.domain.DeliverySummaryEntity

/**
 * This file provides mapping functionality for DeliverySummaryEntity to DeliverySummaryDto.
 */


fun DeliverySummaryEntity.toDto() = DeliverySummaryDto(
    deliveries = deliveries,
    averageMinutesBetweenDeliveryStart = averageMinutesBetweenDeliveryStart
)

