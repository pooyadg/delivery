package com.example.delivery.service

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.controller.dto.DeliveryPatchDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliveryStatus
import com.example.delivery.exception.AlreadyExistsServiceException
import com.example.delivery.exception.NoChangeServiceException
import com.example.delivery.exception.NotAllowedServiceException
import com.example.delivery.exception.ResourceNotFoundServiceException
import com.example.delivery.repository.DeliveryRepository
import com.example.delivery.service.impl.DeliveryServiceImpl
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
    fun addNewDelivery() {

        val deliveryEntitySlot = slot<DeliveryEntity>()
        mockkStatic(UUID::class)

        every { UUID.randomUUID().toString() } returns sampleUuid
        every { deliveryRepository.findByVehicleId(any()) } returns Optional.empty()
        every { deliveryRepository.save(capture(deliveryEntitySlot)) } returns sampleDeliveryEntity

        val actualDeliveryDtoResponse = deliveryService.addNewDelivery(sampleDeliveryDto);

        assertTrue(deliveryEntitySlot.captured.uuid == sampleUuid)
        assertEquals(actualDeliveryDtoResponse, sampleDeliveryEntity.toDto())

        verify(exactly = 1) { deliveryRepository.findByVehicleId(any()) }
        verify(exactly = 1) { deliveryRepository.save(any()) }

    }

    @Test
    fun addNewDelivery_Failure_deliveryAlreadyExists() {

        val deliveryEntitySlot = slot<DeliveryEntity>()
        mockkStatic(UUID::class)

        every { UUID.randomUUID().toString() } returns sampleUuid
        every { deliveryRepository.findByVehicleId(any()) } returns Optional.of(sampleDeliveryEntity)
        every { deliveryRepository.save(capture(deliveryEntitySlot)) } throws AlreadyExistsServiceException(null)

        assertThrows<AlreadyExistsServiceException> {
            deliveryService.addNewDelivery(sampleDeliveryDto)
        }

        verify(exactly = 1) { deliveryRepository.findByVehicleId(any()) }
        verify(exactly = 0) { deliveryRepository.save(any()) }

    }

    @Test
    fun patchDelivery_success() {

        val deliveryEntitySlot = slot<DeliveryEntity>()
        mockkStatic(UUID::class)

        every { deliveryRepository.findByUuid(any()) } returns Optional.of(sampleDeliveryEntity)
        every { deliveryRepository.save(capture(deliveryEntitySlot)) } returns sampleDeliveryEntity

        val actualDeliveryDtoResponse = deliveryService.updateDelivery(sampleUuid, sampleDeliveryPatchDto);

        assertTrue(deliveryEntitySlot.captured.status == DeliveryStatus.valueOf(sampleDeliveryPatchDto.status).id)
        assertTrue(deliveryEntitySlot.captured.finishedAt == sampleDeliveryPatchDto.finishedAt)

        assertEquals(actualDeliveryDtoResponse, sampleDeliveryEntity.toDto())

        verify(exactly = 1) { deliveryRepository.findByUuid(any()) }
        verify(exactly = 1) { deliveryRepository.save(any()) }

    }

    @Test
    fun patchDelivery_failure_NotAllowedServiceException() {

        sampleDeliveryPatchDto.status = "IN_PROGRESS"

        assertThrows<NotAllowedServiceException> {
            deliveryService.updateDelivery(sampleUuid, sampleDeliveryPatchDto)
        }

        verify(exactly = 0) { deliveryRepository.findByUuid(any()) }
        verify(exactly = 0) { deliveryRepository.save(any()) }

    }

    @Test
    fun patchDelivery_failure_ResourceNotFoundServiceException() {

        every { deliveryRepository.findByUuid(any()) } returns Optional.empty()

        assertThrows<ResourceNotFoundServiceException> {
            deliveryService.updateDelivery(sampleUuid, sampleDeliveryPatchDto)
        }

        verify(exactly = 1) { deliveryRepository.findByUuid(any()) }
        verify(exactly = 0) { deliveryRepository.save(any()) }

    }
    @Test
    fun patchDelivery_failure_NoChangeServiceException() {

        sampleDeliveryPatchDto.status = DeliveryStatus.from(sampleDeliveryEntity.status).toString()
        sampleDeliveryPatchDto.finishedAt = sampleDeliveryEntity.finishedAt

        every { deliveryRepository.findByUuid(any()) } returns Optional.of(sampleDeliveryEntity)

        assertThrows<NoChangeServiceException> {
            deliveryService.updateDelivery(sampleUuid, sampleDeliveryPatchDto)
        }

        verify(exactly = 1) { deliveryRepository.findByUuid(any()) }
        verify(exactly = 0) { deliveryRepository.save(any()) }

    }


}