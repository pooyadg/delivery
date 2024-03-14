package com.example.delivery.service

import com.example.delivery.controller.dto.DeliveryDto
import com.example.delivery.controller.dto.DeliveryPatchDto
import com.example.delivery.controller.dto.DeliverySummaryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliveryStatus
import com.example.delivery.exception.*
import com.example.delivery.repository.DeliveryRepository
import com.example.delivery.service.mapper.toDto
import com.example.delivery.service.mapper.toEntity
import jakarta.transaction.Transactional
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.IsolationLevel
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

/**
 *  A service to perform basic CRUD operations for DeliveryEntity
 */
@Service
class DeliveryServiceImpl(private val deliveryRepository: DeliveryRepository) : DeliveryService {

    override fun addNewDelivery(deliveryDto: DeliveryDto): DeliveryDto {
        val deliveryToPersist = deliveryDto.toEntity();

        //TODO: catch IncorrectResultSizeDataAccessException
        val deliveryEntityOptional = deliveryRepository.findByVehicleId(deliveryToPersist.vehicleId)

        if (deliveryEntityOptional.isEmpty) {
            val persistedEntity: DeliveryEntity
            try {
                deliveryToPersist.uuid = UUID.randomUUID().toString();
                persistedEntity = deliveryRepository.save(deliveryToPersist)
            } catch (ex: Exception) {
                //TODO: HTTP status 500
                throw CustomServiceException("An error happened on persisting $deliveryToPersist", ex)
            }
            return persistedEntity.toDto()
        } else {
            //TODO: HTTP status 403
            throw AlreadyExistsServiceException("Vehicle with id= ${deliveryEntityOptional.get().vehicleId} already exists.")
        }

    }

    /**
     * Updates only the finishedAt and status fields of a delivery. And prevents finishedAt update if status is not DELIVERED.
     */
    override fun updateDelivery(id: String, deliveryPatchDto: DeliveryPatchDto): DeliveryDto {


        val deliveryPatchStatus = DeliveryStatus.valueOf(deliveryPatchDto.status.uppercase())
        if (deliveryPatchDto.finishedAt != null && deliveryPatchStatus != DeliveryStatus.DELIVERED) {
            throw NotAllowedServiceException("Updating finishedAt property is not allowed when the status is not DELIVERED")
        }

        val deliveryEntityOptional = deliveryRepository.findByUuid(id)
        if (deliveryEntityOptional.isEmpty) {
            //TODO status 404
            throw ResourceNotFoundServiceException("No delivery found with id= $id")
        }

        val deliveryEntity = deliveryEntityOptional.get()
        if (DeliveryStatus.from(deliveryEntity.status) == deliveryPatchStatus
            && deliveryEntity.finishedAt == deliveryPatchDto.finishedAt
        ) {
            //TODO status 304 looks good
            throw NoChangeServiceException("Nothing to change for the delivery $id with $deliveryPatchDto")
        }

        deliveryEntity.status = deliveryPatchStatus.id
        deliveryEntity.finishedAt = deliveryPatchDto.finishedAt

        val persistedPatch = deliveryRepository.save(deliveryEntity)
        return persistedPatch.toDto()
    }

    @Transactional
    override fun updateDelivery(deliveryPatchDtoList: List<DeliveryPatchDto>): List<DeliveryDto> {

        val responseList= mutableListOf<DeliveryDto>()
        deliveryPatchDtoList.forEach {
            if (it.id == null) {
                throw BadRequestServiceException("Id must be provided.")
            }
            responseList.add(updateDelivery(it.id, it))
        }
        return responseList
    }




}