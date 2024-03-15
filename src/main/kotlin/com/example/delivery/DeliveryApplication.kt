package com.example.delivery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableCaching
class DeliveryApplication

fun main(args: Array<String>) {
	runApplication<DeliveryApplication>(*args)
}
