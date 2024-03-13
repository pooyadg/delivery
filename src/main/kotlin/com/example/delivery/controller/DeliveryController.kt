package com.example.delivery.controller

import com.example.delivery.service.DeliveryService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


/**
 * Controller for REST API endpoints
 */
@RestController
class DeliveryController(private val deliveryService: DeliveryService) {

    @PostMapping("/deliveries")
    fun createDelivery(@RequestBody payload: DeliveryDto): DeliveryDto = deliveryService.addNewDelivery(payload)

}