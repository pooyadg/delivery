package com.example.delivery.controller

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.controller.dto.DeliveryPatchDto
import com.example.delivery.domain.DeliveryStatus
import com.example.delivery.service.DeliveryService
import com.example.delivery.service.SummaryService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import java.time.ZonedDateTime

@WebMvcTest
class DeliveryControllerTest(@Autowired val mockMvc: MockMvc, @Autowired val mapper: ObjectMapper) {

    @MockkBean
    lateinit var deliveryService: DeliveryService

    @MockkBean
    lateinit var summaryService: SummaryService

    private lateinit var sampleUuid: String
    private lateinit var sampleDeliveryDto: DeliveryDto
    private lateinit var sampleDeliveryPatchDto: DeliveryPatchDto

    @BeforeEach
    fun setup() {
        sampleUuid = "69201507-0ae4-4c56-ac2d-75fbe27efad8"
        sampleDeliveryDto = DeliveryDto(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "IN_PROGRESS",
        )
        sampleDeliveryPatchDto = DeliveryPatchDto(
            finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "DELIVERED",
        )
    }

    @Test
    fun whenPostRequestWithDeliveryJson_thenReturnsStatus200() {
        val responseDeliveryDto = sampleDeliveryDto.copy(id = sampleUuid)

        every { deliveryService.addNewDelivery(sampleDeliveryDto) } returns responseDeliveryDto;

        mockMvc.post("/deliveries") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(sampleDeliveryDto)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(responseDeliveryDto)) }
        }

        verify(exactly = 1) { deliveryService.addNewDelivery(sampleDeliveryDto) }
    }


    @Test
    fun whenPatchRequestWithDeliveryPatchJson_thenReturnsStatus200() {
        val responseDeliveryDto = sampleDeliveryDto.copy(id = sampleUuid)
        responseDeliveryDto.status = sampleDeliveryPatchDto.status
        responseDeliveryDto.finishedAt = sampleDeliveryPatchDto.finishedAt

        every { deliveryService.updateDelivery(sampleUuid, sampleDeliveryPatchDto) } returns responseDeliveryDto;

        mockMvc.patch("/deliveries/$sampleUuid") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(sampleDeliveryPatchDto)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(responseDeliveryDto)) }
        }

        verify(exactly = 1) { deliveryService.updateDelivery(sampleUuid, sampleDeliveryPatchDto) }
    }

    @Test
    fun whenPatchRequestWithBulkDeliveryPatchJson_thenReturnsStatus200() {

        every { deliveryService.updateDelivery(getSampleDeliveryPatchList()) } returns getSampleDeliveryResponseList();

        mockMvc.patch("/deliveries/bulk-update") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(getSampleDeliveryPatchList())
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(getSampleDeliveryResponseList())) }
        }

        verify(exactly = 1) { deliveryService.updateDelivery(getSampleDeliveryPatchList()) }
    }


    private fun getSampleDeliveryPatchList() : List<DeliveryPatchDto> =
        listOf(
            DeliveryPatchDto(
                id = "58c68951-114a-42d9-a121-fe55tg",
                finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                status = DeliveryStatus.DELIVERED.toString()
            ),
            DeliveryPatchDto(
                id = "hfdr4951-114a-42d9-a121-3756455d9e",
                finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                status = DeliveryStatus.DELIVERED.toString()
            ),
            DeliveryPatchDto(
                id = "456456951-114a-42d9-a121-3dssdyh7",
                finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                status = DeliveryStatus.DELIVERED.toString()
            )
        )


    private fun getSampleDeliveryResponseList() : List<DeliveryDto> =
        listOf(
            DeliveryDto(
                id = "58c68951-114a-42d9-a121-fe55tg",
                vehicleId = "AHV-501",
                startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                status = DeliveryStatus.DELIVERED.toString()
            ),
            DeliveryDto(
                id = "hfdr4951-114a-42d9-a121-3756455d9e",
                vehicleId = "AHV-502",
                startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                status = DeliveryStatus.DELIVERED.toString()
            ),
            DeliveryDto(
                id = "456456951-114a-42d9-a121-3dssdyh7",
                vehicleId = "AHV-503",
                startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
                status = DeliveryStatus.DELIVERED.toString()
            )
        )



}