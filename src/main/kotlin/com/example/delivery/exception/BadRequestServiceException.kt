package com.example.delivery.exception

class BadRequestServiceException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)