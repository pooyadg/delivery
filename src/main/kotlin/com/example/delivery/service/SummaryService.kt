package com.example.delivery.service

import com.example.delivery.controller.dto.DeliverySummaryDto
import com.example.delivery.domain.DeliveryEntity
import com.example.delivery.domain.DeliverySummaryEntity
import com.example.delivery.repository.SummaryRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


interface SummaryService {


    fun getDeliveriesForDay(day: LocalDateTime): List<DeliveryEntity>

    fun getDeliverySummary(): DeliverySummaryDto

}