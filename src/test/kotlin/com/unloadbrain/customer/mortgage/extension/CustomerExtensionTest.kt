package com.unloadbrain.customer.mortgage.extension

import com.unloadbrain.customer.mortgage.dto.response.CustomerResponse
import com.unloadbrain.customer.mortgage.entity.Customer
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit.YEARS
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

class CustomerExtensionTest {

    @Test
    fun `should convert Customer to CustomerResponse with updated age`() {
        // Given
        val customerId = randomUUID()
        val customer = Customer(customerId, "John", "Address", "accountId", 30).apply {
            createdAt = LocalDateTime.now().minus(5, YEARS)
            updatedAt = LocalDateTime.now().minus(5, YEARS)
        }

        // When
        val result: CustomerResponse = customer.toCustomerResponse()

        // Then
        assertEquals(customerId, result.id)
        assertEquals("John", result.name)
        assertEquals("Address", result.address)
        assertEquals("accountId", result.accountId)

        // Validate age calculation based on the creationDateTime
        assertEquals(35, result.age)
    }
}
