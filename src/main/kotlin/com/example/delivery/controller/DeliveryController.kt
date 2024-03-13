package com.example.delivery.controller

import com.example.delivery.service.DeliveryService
import org.springframework.web.bind.annotation.*


/**
 * Controller for REST API endpoints
 */
@RestController
class DeliveryController(private val deliveryService: DeliveryService) {

    @PostMapping("/deliveries")
    fun createDelivery(@RequestBody payload: DeliveryDto): DeliveryDto = deliveryService.addNewDelivery(payload)

    @PatchMapping("deliveries/{id}")
    fun updateDelivery(@RequestBody payload: DeliveryPatchDto, @PathVariable("id") id: String): DeliveryDto = deliveryService.updateDelivery(id, payload)

    @PatchMapping("deliveries/bulk-update")
    fun updateDelivery(@RequestBody payload: List<DeliveryDto>): DeliveryDto = deliveryService.updateDelivery(payload)

    @GetMapping("deliveries/business-summary")
    fun getDeliverySummary(): DeliverySummaryDto = deliveryService.getDeliverySummary()


}