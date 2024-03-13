package com.example.delivery.exception

class CustomServiceException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)