package com.example.delivery.service

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.controller.dto.DeliveryPatchDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliveryStatus
import com.example.delivery.util.DeliveryUtil.Companion.calculateAverageMinutesBetweenDeliveryStart
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime

class SummarySchedulerTest {


    private lateinit var sampleUuid: String
    private lateinit var sampleDeliveryDto: DeliveryDto
    private lateinit var sampleDeliveryEntity: DeliveryEntity
    private lateinit var sampleDeliveryPatchDto: DeliveryPatchDto


    @BeforeEach
    fun setup() {
        sampleUuid = "69201507-0ae4-4c56-ac2d-75fbe27efad8"
        sampleDeliveryDto = DeliveryDto(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "IN_PROGRESS",
        )

        sampleDeliveryEntity = DeliveryEntity(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = DeliveryStatus.IN_PROGRESS.id,
            uuid = sampleUuid
        )

        sampleDeliveryPatchDto = DeliveryPatchDto(
            finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "DELIVERED",
        )


    }

    @Test
    fun `test calculateAverageMinutesBetweenDeliveryStart with empty list`() {
        val deliveries = emptyList<DeliveryEntity>()
        val averageMinutes = calculateAverageMinutesBetweenDeliveryStart(deliveries)
        assertEquals(0, averageMinutes)
    }

    @Test
    fun `test calculateAverageMinutesBetweenDeliveryStart with single delivery`() {
        val deliveries = listOf(
            sampleDeliveryEntity.copy()
        )
        val averageMinutes = calculateAverageMinutesBetweenDeliveryStart(deliveries)
        assertEquals(0, averageMinutes)
    }

    @Test
    fun `test calculateAverageMinutesBetweenDeliveryStart with multiple deliveries`() {
        val startTime1 = ZonedDateTime.parse("2023-10-09T01:00:00.0Z")
        val startTime2 = ZonedDateTime.parse("2023-10-09T03:00:00.0Z")
        val startTime3 = ZonedDateTime.parse("2023-10-09T09:00:00.0Z")
        val deliveries = listOf(
            sampleDeliveryEntity.copy(startedAt = startTime1),
            sampleDeliveryEntity.copy(startedAt = startTime2),
            sampleDeliveryEntity.copy(startedAt = startTime3),
            )
        val averageMinutes = calculateAverageMinutesBetweenDeliveryStart(deliveries)
        assertEquals(240, averageMinutes)
    }
}