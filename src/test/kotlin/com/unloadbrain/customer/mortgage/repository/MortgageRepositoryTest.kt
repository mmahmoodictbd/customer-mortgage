package com.unloadbrain.customer.mortgage.repository

import com.unloadbrain.customer.mortgage.entity.Customer
import com.unloadbrain.customer.mortgage.entity.Mortgage
import com.unloadbrain.customer.mortgage.entity.Mortgage.MortgageType
import com.unloadbrain.customer.mortgage.service.UuidService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MortgageRepositoryTest(
    @Autowired val uuidService: UuidService,
    @Autowired val customerRepository: CustomerRepository,
    @Autowired val mortgageRepository: MortgageRepository
) {

    private lateinit var customerId: UUID
    private lateinit var mortgageId: UUID

    @BeforeAll
    fun setUp() {
        val customer = Customer(
            id = uuidService.getRandomUuid(),
            name = "John Doe",
            address = "Amsterdam",
            accountId = "test-account-1",
            age = 33
        ).also {
            customerRepository.saveAndFlush(it)
            customerId = it.id
        }

        Mortgage(
            id = uuidService.getRandomUuid(),
            mortgageSum = 100000.toBigDecimal(),
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusYears(30),
            interestPercentage = 3.5.toBigDecimal(),
            mortgageType = MortgageType.ANN,
            customers = setOf(customer)
        ).also {
            mortgageRepository.saveAndFlush(it)
            mortgageId = it.id
        }
    }

    @AfterAll
    fun cleanup() {
        mortgageRepository.deleteAll()
    }

    @Test
    fun `should find mortgage by ID`() {
        // When
        val foundMortgage = mortgageRepository.findById(mortgageId)

        // Then
        assertNotNull(foundMortgage)
        assertEquals(customerId, foundMortgage.get().customers.first().id)
    }
}