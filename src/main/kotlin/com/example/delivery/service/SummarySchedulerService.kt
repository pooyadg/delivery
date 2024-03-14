package com.example.delivery.service

import com.example.delivery.domain.DeliverySummaryEntity
import com.example.delivery.repository.SummaryRepository
import com.example.delivery.util.DeliveryUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class SummarySchedulerService(
    private val summaryService: SummaryService,
    private val summaryRepository: SummaryRepository,
    @Value("\${scheduler.cron.expression}") private val cronExpression: String,
    @Value("\${scheduler.time.zone}") private val timeZone: String
) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "\${scheduler.cron.expression}", zone = "\${scheduler.time.zone}")
    fun generateAndInsertSummaryReport() {
        logger.debug("generateAndInsertSummaryReport started!")
        val yesterday = LocalDateTime.now(ZoneId.of(timeZone)).minusDays(1)
        val deliveries = summaryService.getDeliveriesForDay(yesterday)
        val averageMinutesBetweenDeliveryStart = DeliveryUtil.calculateAverageMinutesBetweenDeliveryStart(deliveries)

        val summary = DeliverySummaryEntity(
            deliveries = deliveries.size,
            averageMinutesBetweenDeliveryStart = averageMinutesBetweenDeliveryStart.toInt(),
            createdAt = LocalDateTime.now()
        )
        logger.debug("{} is going to be registered!", summary)
        summaryRepository.save(summary)
    }

}
