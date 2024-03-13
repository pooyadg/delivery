package com.example.delivery.service

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.controller.dto.DeliveryPatchDto
import com.example.delivery.controller.dto.DeliverySummaryDto


interface DeliveryService {

    /* Persists a new DeliveryEntity if there is no one with the provided Vehicle id in the incoming [deliveryDto] */
    fun addNewDelivery(deliveryDto: DeliveryDto): DeliveryDto

    /* Updates a delivery. */
    fun updateDelivery(id: String, deliveryPatchDto: DeliveryPatchDto): DeliveryDto

    /* Updates multiple deliveries. */
    fun updateDelivery(deliveryPatchDtoList: List<DeliveryPatchDto>): List<DeliveryDto>

    /* Provides summary of the deliveries */
    fun getDeliverySummary(): DeliverySummaryDto

}