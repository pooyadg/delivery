package com.example.delivery.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZonedDateTime

/**
 * Represents the database entity for storing the delivery details.
 */
@Entity
@Table(name = "delivery")
data class DeliveryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = 0,

    @Column(name = "uuid", unique = true, nullable = false)
    var uuid: String? = "",

    @Column(name = "vehicle_id", unique = true, nullable = false)
    val vehicleId: String,

    @Column(name = "started_at", nullable = false)
    val startedAt: ZonedDateTime,

    @Column(name = "finished_at", nullable = true)
    val finishedAt: ZonedDateTime? = null,

    @Column(name = "status", nullable = false)
    val status: Byte,
)