package com.example.delivery.repository

import com.example.delivery.domain.DeliverySummaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SummaryRepository : JpaRepository<DeliverySummaryEntity, Long> {
    fun findTopByOrderByCreatedAtDesc(): DeliverySummaryEntity
}