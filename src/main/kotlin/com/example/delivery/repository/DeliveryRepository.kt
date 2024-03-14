package com.example.delivery.repository

import com.example.delivery.domain.DeliveryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.Optional

@Repository
interface DeliveryRepository : JpaRepository<DeliveryEntity, Long> {
    fun findByVehicleId(vehicleId: String?): Optional<DeliveryEntity>
    fun findByUuid(uuid: String?): Optional<DeliveryEntity>

    @Query("SELECT d FROM DeliveryEntity d WHERE DATE(d.startedAt) = :day")
    fun findByStartedAtDay(@Param("day") day: LocalDateTime): List<DeliveryEntity>
}