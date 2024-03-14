package com.example.delivery.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Entity
@Table(name = "delivery_summary")
data class DeliverySummaryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @Column(name = "deliveries")
    val deliveries: Int,

    @Column(name = "average_minutes_between_delivery_start")
    val averageMinutesBetweenDeliveryStart: Int,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime?

)
