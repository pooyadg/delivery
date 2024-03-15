package com.example.delivery.controller.dto

import org.springframework.http.HttpStatusCode

class ErrorDto(
    var code: Int,
    var status: HttpStatusCode,
    var message: String? = null
)