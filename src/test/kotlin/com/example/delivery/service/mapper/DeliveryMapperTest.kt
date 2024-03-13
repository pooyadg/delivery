package com.example.delivery.service.mapper

import com.example.delivery.controller.DeliveryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliveryStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class DeliveryMapperTest {

    @Test
    fun mapDeliveryDtoToEntity() {
        val deliveryDto = DeliveryDto(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "IN_PROGRESS",
        )

        val deliveryEntity = DeliveryEntity(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = DeliveryStatus.IN_PROGRESS.id,
        )

        Assertions.assertEquals(deliveryDto.toEntity(),deliveryEntity)
    }

    @Test
    fun mapDeliveryEntityToDto() {
        val deliveryDto = DeliveryDto(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "IN_PROGRESS",
            id = "69201507-0ae4-4c56-ac2d-75fbe27efad8"
        )

        val deliveryEntity = DeliveryEntity(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = DeliveryStatus.IN_PROGRESS.id,
            uuid = "69201507-0ae4-4c56-ac2d-75fbe27efad8"
        )

        Assertions.assertEquals(deliveryEntity.toDto(), deliveryDto)
    }

}