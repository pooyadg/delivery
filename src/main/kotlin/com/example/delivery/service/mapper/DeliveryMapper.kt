package com.example.delivery.service.mapper

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliveryStatus

/**
 * This file provides mapping functionality for DeliveryEntity to DeliveryDto and vise versa.
 */

fun DeliveryDto.toEntity() = DeliveryEntity(
    vehicleId = vehicleId,
    startedAt = startedAt,
    finishedAt = finishedAt,
    status = DeliveryStatus.valueOf(status.uppercase()).id,
)

fun DeliveryEntity.toDto() = DeliveryDto(
    id = uuid,
    vehicleId = vehicleId,
    startedAt = startedAt,
    finishedAt = finishedAt,
    status = (DeliveryStatus from status).toString(),
)

