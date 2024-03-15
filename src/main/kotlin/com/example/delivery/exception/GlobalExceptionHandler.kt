package com.example.delivery.exception

import com.example.delivery.controller.dto.ErrorDto
import jakarta.servlet.ServletException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.resource.NoResourceFoundException

@ControllerAdvice
class GlobalExceptionHandler {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(value = [Exception::class, CustomServiceException::class])
    fun handleGlobalException(ex: Exception?, request: WebRequest?): ResponseEntity<ErrorDto> {
        logger.error(request.toString(), ex)
        val errorDto = ErrorDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            "There is an internal error, please try again later."
        )
        return ResponseEntity<ErrorDto>(errorDto, errorDto.status)
    }

    @ExceptionHandler(
        value = [
            ServletException::class,
        ]
    )
    fun handleBadRequestException(ex: Exception?, request: WebRequest?): ResponseEntity<ErrorDto> {
        logger.error(request.toString(), ex)
        val errorDto =
            ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                "This service does not support this request, please double check your request."
            )
        return ResponseEntity<ErrorDto>(errorDto, errorDto.status)
    }

    @ExceptionHandler(ResourceNotFoundServiceException::class)
    fun handleResourceNotFoundException(ex: Exception?, request: WebRequest?): ResponseEntity<ErrorDto> {
        logger.error(request.toString(), ex)
        val errorDto =
            ErrorDto(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND,
                "The requested resource is not found, please double check your request."
            )
        return ResponseEntity<ErrorDto>(errorDto, errorDto.status)
    }

    @ExceptionHandler(NoChangeServiceException::class)
    fun handleNoChangeServiceException(ex: Exception?, request: WebRequest?): ResponseEntity<ErrorDto> {
        logger.error(request.toString(), ex)
        val errorDto =
            ErrorDto(
                HttpStatus.NOT_MODIFIED.value(),
                HttpStatus.NOT_MODIFIED,
                ex?.message
            )
        return ResponseEntity<ErrorDto>(errorDto, errorDto.status)
    }


    @ExceptionHandler(AlreadyExistsServiceException::class)
    fun handleAlreadyExistsServiceException(ex: Exception?, request: WebRequest?): ResponseEntity<ErrorDto> {
        logger.error(request.toString(), ex)
        val errorDto =
            ErrorDto(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN,
                ex?.message
            )
        return ResponseEntity<ErrorDto>(errorDto, errorDto.status)
    }

}