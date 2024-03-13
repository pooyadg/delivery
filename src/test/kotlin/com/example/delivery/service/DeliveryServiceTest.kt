package com.example.delivery.service

import com.example.delivery.controller.DeliveryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliveryStatus
import com.example.delivery.repository.DeliveryRepository
import com.example.delivery.service.mapper.toDto
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.ZonedDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class DeliveryServiceTest {


    @MockK
    lateinit var deliveryRepository: DeliveryRepository


    @InjectMockKs
    lateinit var deliveryService: DeliveryServiceImpl


    private lateinit var sampleUuid: String
    private lateinit var sampleDeliveryDto: DeliveryDto
    private lateinit var sampleDeliveryEntity: DeliveryEntity


    @BeforeEach
    fun setup(){
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
    }

    @Test
    fun addNewDelivery() {

        val deliveryEntitySlot = slot<DeliveryEntity>()
        mockkStatic(UUID::class)

        every { UUID.randomUUID().toString() } returns sampleUuid
        every { deliveryRepository.findByVehicleId(any()) } returns Optional.empty()
        every { deliveryRepository.save(capture(deliveryEntitySlot)) } returns sampleDeliveryEntity

        val actualDeliveryDtoResponse = deliveryService.addNewDelivery(sampleDeliveryDto);

        assertTrue(deliveryEntitySlot.captured.uuid == sampleUuid)
        assertEquals(actualDeliveryDtoResponse, sampleDeliveryEntity.toDto())

        verify (exactly = 1) { deliveryRepository.findByVehicleId(any()) }
        verify (exactly = 1) { deliveryRepository.save(any()) }

    }

    @Test
    fun addNewDelivery_deliveryExist() {

        val deliveryEntitySlot = slot<DeliveryEntity>()
        mockkStatic(UUID::class)

        every { UUID.randomUUID().toString() } returns sampleUuid
        every { deliveryRepository.findByVehicleId(any()) } returns Optional.of(sampleDeliveryEntity)
        every { deliveryRepository.save(capture(deliveryEntitySlot)) } throws RuntimeException()

        assertThrows<RuntimeException>{
            deliveryService.addNewDelivery(sampleDeliveryDto)
        }

        verify (exactly = 1) { deliveryRepository.findByVehicleId(any()) }
        verify (exactly = 0) { deliveryRepository.save(any()) }

    }
}