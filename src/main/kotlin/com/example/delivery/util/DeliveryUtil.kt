package com.example.delivery.util

import com.example.delivery.domain.DeliveryEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.temporal.ChronoUnit

class DeliveryUtil {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
        fun calculateAverageMinutesBetweenDeliveryStart(deliveries: List<DeliveryEntity>): Long {
            logger.debug("calculateAverageMinutesBetweenDeliveryStart started!")
            if (deliveries.isEmpty()) return 0

            if (deliveries.size == 1) return 0;

            val sortedDeliveries = deliveries.sortedBy { it.startedAt }
            var totalMinutes = 0L
            for (i in 1 until sortedDeliveries.size) {
                val minutesBetween =
                    ChronoUnit.MINUTES.between(sortedDeliveries[i - 1].startedAt, sortedDeliveries[i].startedAt)
                totalMinutes += minutesBetween
            }
            return totalMinutes / (deliveries.size - 1)
        }
    }
}