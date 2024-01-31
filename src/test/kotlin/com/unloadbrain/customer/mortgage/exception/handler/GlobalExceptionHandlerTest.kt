package com.unloadbrain.customer.mortgage.exception.handler

import com.unloadbrain.customer.mortgage.exception.AccountIdAlreadyExistException
import com.unloadbrain.customer.mortgage.exception.ResourceNotFoundException
import com.unloadbrain.customer.mortgage.exception.ServiceUnavailableException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ProblemDetail

@ExtendWith(MockitoExtension::class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private lateinit var globalExceptionHandler: GlobalExceptionHandler

    @Test
    fun `badRequest should return BadRequest ProblemDetail`() {
        // Given
        val accountIdAlreadyExistException = AccountIdAlreadyExistException("AccountId already exists")

        // When
        val result: ProblemDetail = globalExceptionHandler.badRequest(accountIdAlreadyExistException)

        // Then
        assertEquals(BAD_REQUEST.value(), result.status)
        assertEquals("AccountId already exists", result.detail)
    }

    @Test
    fun `resourceNotFoundException should return NotFound ProblemDetail`() {
        // Given
        val resourceNotFoundException = ResourceNotFoundException("Resource not found")

        // When
        val result: ProblemDetail = globalExceptionHandler.notFound(resourceNotFoundException)

        // Then
        assertEquals(NOT_FOUND.value(), result.status)
        assertEquals("Resource not found", result.detail)
    }

    @Test
    fun `unknownIntegrationError should return InternalServerError ProblemDetail`() {
        // Given
        val integrationException = ServiceUnavailableException("Integration error")

        // When
        val result: ProblemDetail = globalExceptionHandler.unknownIntegrationError(integrationException)

        // Then
        assertEquals(INTERNAL_SERVER_ERROR.value(), result.status)
        assertEquals("Integration error", result.detail)
    }

    @Test
    fun `unknownError should return InternalServerError ProblemDetail`() {
        // Given
        val throwable = Throwable("Unknown error")

        // When
        val result: ProblemDetail = globalExceptionHandler.unknownError(throwable)

        // Then
        assertEquals(INTERNAL_SERVER_ERROR.value(), result.status)
        assertEquals("Unknown error", result.detail)
    }
}
