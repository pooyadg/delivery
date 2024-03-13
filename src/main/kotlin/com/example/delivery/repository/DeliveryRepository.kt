package com.example.delivery.repository

import com.example.delivery.domain.DeliveryEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DeliveryRepository : CrudRepository<DeliveryEntity, Long> {
    fun findByVehicleId(vehicleId: String): Optional<DeliveryEntity>
}