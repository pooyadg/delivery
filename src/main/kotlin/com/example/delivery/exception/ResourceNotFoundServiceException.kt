package com.example.delivery.exception

class ResourceNotFoundServiceException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)