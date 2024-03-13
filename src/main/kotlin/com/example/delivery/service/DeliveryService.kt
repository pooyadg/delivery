package com.example.delivery.service

import com.example.delivery.controller.DeliveryDto
import com.example.delivery.controller.DeliveryPatchDto
import com.example.delivery.controller.DeliverySummaryDto
import org.springframework.stereotype.Service


interface DeliveryService {

    /* Persists a new DeliveryEntity if there is no one with the provided Vehicle id in the incoming [deliveryDto] */
    fun addNewDelivery(deliveryDto: DeliveryDto): DeliveryDto

    /* Updates a delivery. */
    fun updateDelivery(id: String, deliveryPatchDto: DeliveryPatchDto): DeliveryDto

    /* Updates multiple deliveries. */
    fun updateDelivery(deliveryDtoList: List<DeliveryDto>): DeliveryDto

    /* Provides summary of the deliveries */
    fun getDeliverySummary(): DeliverySummaryDto

}