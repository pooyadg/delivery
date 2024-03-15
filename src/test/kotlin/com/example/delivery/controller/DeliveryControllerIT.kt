package com.example.delivery.controller

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.controller.dto.DeliveryPatchDto
import com.example.delivery.controller.dto.DeliverySummaryDto
import com.example.delivery.postgres
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.ZonedDateTime


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Testcontainers
class DeliveryControllerIT(
    @Autowired val client: TestRestTemplate,
    @Autowired val jdbc: JdbcTemplate,
) {
    @AfterEach
    fun cleanup() {
        jdbc.execute("truncate table delivery")
        jdbc.execute("truncate table delivery_summary")
    }

    companion object {
        @Container
        val container = postgres("15") {
            withDatabaseName("deliveries")
            withUsername("postgres")
            withPassword("postgres")
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.datasource.username", container::getUsername)
        }
    }


    @Test
    fun testIfContainerIsUpAndRunning() {
        Assertions.assertTrue(container.isRunning)
    }

    @Test
    fun endpointTest() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(null, headers)
        val responseEntity: ResponseEntity<DeliverySummaryDto> = client.exchange(
            url = "/deliveries/business-summary",
            method = HttpMethod.GET,
            request,
            DeliverySummaryDto::class
        )
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isEqualTo(DeliverySummaryDto())
    }


    @Test
    fun testPostDelivery() {
        val sampleDeliveryDto = DeliveryDto(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "IN_PROGRESS",
        )
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(sampleDeliveryDto, headers)
        val responseEntity: ResponseEntity<DeliveryDto> = client.exchange(
            url = "/deliveries",
            method = HttpMethod.POST,
            request,
            DeliveryDto::class
        )

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).toString().contains(sampleDeliveryDto.vehicleId)
        assertThat(responseEntity.body).toString().contains(sampleDeliveryDto.status)
    }


    @Test
    fun testPatchOneDelivery() {
        val sampleDeliveryDto = DeliveryDto(
            vehicleId = "AHV-589",
            startedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "IN_PROGRESS",
        )
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(sampleDeliveryDto, headers)
        val postResponseEntity: ResponseEntity<DeliveryDto> = client.exchange(
            url = "/deliveries",
            method = HttpMethod.POST,
            request,
            DeliveryDto::class
        )

        val samplePatchDeliveryDto = DeliveryPatchDto(
            finishedAt = ZonedDateTime.parse("2023-10-09T12:45:34.678Z"),
            status = "DELIVERED",
        )

        headers.contentType = MediaType.APPLICATION_JSON
        val pathRequest = HttpEntity(samplePatchDeliveryDto, headers)
        val responseEntityForPatch: ResponseEntity<DeliveryDto> = client.exchange(
            url = "/deliveries/{id}",
            method = HttpMethod.PATCH,
            pathRequest,
            postResponseEntity.body?.id!!
        )

        assertThat(responseEntityForPatch.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntityForPatch.body).toString().contains(sampleDeliveryDto.vehicleId)
        assertThat(responseEntityForPatch.body).toString().contains(samplePatchDeliveryDto.status)
    }


}