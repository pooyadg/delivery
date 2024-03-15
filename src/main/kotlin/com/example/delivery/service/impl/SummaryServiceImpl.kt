package com.example.delivery.service.impl

import com.example.delivery.controller.dto.DeliverySummaryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliverySummaryEntity
import com.example.delivery.repository.DeliveryRepository
import com.example.delivery.repository.SummaryRepository
import com.example.delivery.service.SummaryService
import com.example.delivery.service.mapper.toDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SummaryServiceImpl(
    private val deliveryRepository: DeliveryRepository,
    private val summaryRepository: SummaryRepository
) : SummaryService {
    override fun getDeliveriesForDay(day: LocalDateTime): List<DeliveryEntity> {
        return deliveryRepository.findByStartedAtDay(day)
    }

    @Cacheable("summaryCache", key = "#day.dayOfYear")
    override fun getDeliverySummary(day: LocalDateTime): DeliverySummaryDto {
        val deliverySummaryEntity: DeliverySummaryEntity

        try {
            deliverySummaryEntity = summaryRepository.findFirstByCreatedAtDay(day)!!
        } catch (exception: Exception) {
            return DeliverySummaryDto()
        }
        return deliverySummaryEntity.toDto()
    }

}