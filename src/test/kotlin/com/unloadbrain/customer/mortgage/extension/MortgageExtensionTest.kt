package com.unloadbrain.customer.mortgage.extension

import com.unloadbrain.customer.mortgage.dto.response.MortgageResponse
import com.unloadbrain.customer.mortgage.entity.Customer
import com.unloadbrain.customer.mortgage.entity.Mortgage
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

class MortgageExtensionTest {

    @Test
    fun `should convert Mortgage to MortgageResponse with mapped customers`() {
        // Given
        val mortgageId = randomUUID()
        val startDate = LocalDate.now()
        val endDate = LocalDate.now().plusYears(30)
        val mortgage = Mortgage(
            id = mortgageId,
            mortgageSum = 100000.toBigDecimal(),
            startDate = startDate,
            endDate = endDate,
            interestPercentage = 3.5.toBigDecimal(),
            mortgageType = Mortgage.MortgageType.ANN,
            customers = setOf(
                Customer(randomUUID(), "John Doe", "Amsterdam", "account1", 30),
                Customer(randomUUID(), "Jane Doe", "Amsterdam", "account2", 40)
            )
        )

        // When
        val result: MortgageResponse = mortgage.toMortgageResponse()

        // Then
        assertEquals(mortgageId, result.id)
        assertEquals(100000.toBigDecimal(), result.mortgageSum)
        assertEquals(startDate, result.startDate)
        assertEquals(endDate, result.endDate)
        assertEquals(3.5.toBigDecimal(), result.interestPercentage)
        assertEquals(Mortgage.MortgageType.ANN, result.mortgageType)

        // Validate customers mapping
        assertEquals(2, result.customers.size)

        val firstCustomer = result.customers.first { it.id == mortgage.customers.first().id }
        assertEquals("John Doe", firstCustomer.name)
        assertEquals("account1", firstCustomer.accountId)

        val secondCustomer = result.customers.first { it.id == mortgage.customers.elementAt(1).id }
        assertEquals("Jane Doe", secondCustomer.name)
        assertEquals("account2", secondCustomer.accountId)
    }
}
