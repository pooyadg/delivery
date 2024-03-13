package com.example.delivery.service

import com.example.delivery.controller.DeliveryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.repository.DeliveryRepository
import com.example.delivery.service.mapper.toDto
import com.example.delivery.service.mapper.toEntity
import org.springframework.stereotype.Service
import java.util.*

/**
 *  A service to perform basic CRUD operations for DeliveryEntity
 */
class DeliveryServiceImpl(private val deliveryRepository: DeliveryRepository) : DeliveryService {


    override fun addNewDelivery(deliveryDto: DeliveryDto): DeliveryDto {
        val deliveryToPersist = deliveryDto.toEntity();

        //TODO: catch IncorrectResultSizeDataAccessException
        val deliveryEntity = deliveryRepository.findByVehicleId(deliveryToPersist.vehicleId)

        if (deliveryEntity.isEmpty) {
            val persistedEntity: DeliveryEntity
            try {
                deliveryToPersist.uuid = UUID.randomUUID().toString();
                persistedEntity = deliveryRepository.save(deliveryToPersist)
            } catch (ex: Exception) {
                //TODO: define CustomException and handle later in a ControllerAdvice
                throw RuntimeException(ex)
            }
            return persistedEntity.toDto()
        } else {
            //TODO: define CustomException and handle later in a ControllerAdvice
            throw RuntimeException("Vehicle with id= ${deliveryEntity.get().vehicleId} already exists.")
        }

    }

}