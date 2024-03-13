package com.example.delivery.controller

import com.example.delivery.service.DeliveryService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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

}