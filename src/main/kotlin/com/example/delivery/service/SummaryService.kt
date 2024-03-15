package com.example.delivery.service

import com.example.delivery.controller.dto.DeliverySummaryDto
import com.example.delivery.domain.DeliveryEntity
import java.time.LocalDateTime


interface SummaryService {


    fun getDeliveriesForDay(day: LocalDateTime): List<DeliveryEntity>

    fun getDeliverySummary(day: LocalDateTime): DeliverySummaryDto

}