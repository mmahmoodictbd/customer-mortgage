package com.unloadbrain.customer.mortgage.exception.handler

import com.unloadbrain.customer.mortgage.exception.AccountIdAlreadyExistException
import com.unloadbrain.customer.mortgage.exception.AgifyUnknownException
import com.unloadbrain.customer.mortgage.exception.BadRequestException
import com.unloadbrain.customer.mortgage.exception.IntegrationException
import com.unloadbrain.customer.mortgage.exception.ResourceNotFoundException
import com.unloadbrain.customer.mortgage.exception.ServiceUnavailableException
import com.unloadbrain.customer.mortgage.extension.toProblemDetail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun handleMethodArgumentValidationExceptions(exception: MethodArgumentNotValidException): ProblemDetail {
        val errors = exception.bindingResult.fieldErrors.map { fieldError ->
            "${fieldError.field}: ${fieldError.defaultMessage}"
        }
        return ProblemDetail.forStatusAndDetail(BAD_REQUEST, errors.joinToString(", "))
    }

    @ExceptionHandler(AccountIdAlreadyExistException::class)
    fun badRequest(ex: BadRequestException): ProblemDetail =
        ex.toProblemDetail(BAD_REQUEST).also { log.error(it.detail, ex) }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun notFound(ex: ResourceNotFoundException): ProblemDetail =
        ex.toProblemDetail(NOT_FOUND).also { log.error(it.detail, ex) }

    @ExceptionHandler(ServiceUnavailableException::class, AgifyUnknownException::class)
    fun unknownIntegrationError(ex: IntegrationException): ProblemDetail =
        ex.toProblemDetail(INTERNAL_SERVER_ERROR).also { log.error(it.detail, ex) }

    @ExceptionHandler(Throwable::class)
    fun unknownError(ex: Throwable): ProblemDetail =
        ex.toProblemDetail(INTERNAL_SERVER_ERROR).also { log.error(it.detail, ex) }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}