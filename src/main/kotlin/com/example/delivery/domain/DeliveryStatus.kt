package com.example.delivery.domain

enum class DeliveryStatus(val id: Byte) {
    IN_PROGRESS (10),
    DELIVERED(20);


    companion object {
        private val map = entries.associateBy { it.id }
        infix fun from(id: Byte) = map[id]
    }

}