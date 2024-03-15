package com.example.delivery.repository

import com.example.delivery.domain.DeliverySummaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface SummaryRepository : JpaRepository<DeliverySummaryEntity, Long> {
    @Query("SELECT s FROM DeliverySummaryEntity s WHERE DATE(s.createdAt) = :day ORDER BY  s.createdAt desc LIMIT 1")
    fun findFirstByCreatedAtDay(@Param("day") day: LocalDateTime): DeliverySummaryEntity?
}