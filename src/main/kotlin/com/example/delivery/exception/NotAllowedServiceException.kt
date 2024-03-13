package com.example.delivery.exception

class NotAllowedServiceException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)