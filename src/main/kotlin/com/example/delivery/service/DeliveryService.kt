package com.example.delivery.service

import com.example.delivery.controller.DeliveryDto
import org.springframework.stereotype.Service

@Service
interface DeliveryService {

    /**
     * Persists a new DeliveryEntity if there is no one with the provided Vehicle id in the incoming [deliveryDto]
     */
    fun addNewDelivery(deliveryDto: DeliveryDto): DeliveryDto

}