package com.example.status.error

import com.example.status.config.RequestIdFilter
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(InvalidStatusException::class)
    fun invalidStatus(exception: InvalidStatusException, request: HttpServletRequest): ResponseEntity<ApiError> {
        logger.warn("event=invalid_status_filter path={}", request.requestURI)
        return error(HttpStatus.BAD_REQUEST, exception.message!!, request)
    }

    @ExceptionHandler(Exception::class)
    fun unexpected(exception: Exception, request: HttpServletRequest): ResponseEntity<ApiError> {
        if (exception is ErrorResponse) {
            val status = HttpStatus.valueOf(exception.statusCode.value())
            return error(status, status.reasonPhrase, request)
        }
        logger.error("event=unexpected_server_error path={}", request.requestURI, exception)
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again.", request)
    }

    private fun error(status: HttpStatus, message: String, request: HttpServletRequest) =
        ResponseEntity.status(status).body(ApiError(Instant.now(), status.value(), status.reasonPhrase, message,
            request.requestURI, request.getAttribute(RequestIdFilter.ATTRIBUTE) as? String ?: "unavailable"))
}
