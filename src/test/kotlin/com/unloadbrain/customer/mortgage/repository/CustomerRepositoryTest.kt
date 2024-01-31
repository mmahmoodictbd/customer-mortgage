package com.unloadbrain.customer.mortgage.repository

import com.unloadbrain.customer.mortgage.entity.Customer
import com.unloadbrain.customer.mortgage.entity.Mortgage
import com.unloadbrain.customer.mortgage.service.UuidService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDate
import java.util.UUID
import kotlin.test.assertNull

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerRepositoryTest(
    @Autowired val uuidService: UuidService,
    @Autowired val customerRepository: CustomerRepository,
    @Autowired val mortgageRepository: MortgageRepository
) {

    private lateinit var customerId: UUID
    private lateinit var mortgageId: UUID

    @BeforeEach
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
            mortgageType = Mortgage.MortgageType.ANN,
            customers = setOf(customer)
        ).also {
            mortgageRepository.saveAndFlush(it)
            mortgageId = it.id
        }

        Customer(
            id = uuidService.getRandomUuid(),
            name = "Jane Doe",
            address = "Amsterdam",
            accountId = "test-account-2",
            age = 33
        ).also {
            customerRepository.saveAndFlush(it)
        }
    }

    @AfterEach
    fun cleanup() {
        customerRepository.deleteAll()
        mortgageRepository.deleteAll()
    }

    @Test
    fun `findByAccountId should return customer when account ID exists`() {
        // Given
        val accountId = "test-account-1"

        // When
        val customer = customerRepository.findByAccountId(accountId)

        // Then
        assertEquals("John Doe", customer?.name)
    }

    @Test
    fun `findByAccountId should return null when account ID does not exist`() {
        // Given
        val nonExistingAccountId = "non-existing-account-id"

        // When
        val customer = customerRepository.findByAccountId(nonExistingAccountId)

        // Then
        assertEquals(null, customer)
    }

    @Test
    fun `findAllByAccountIdIn should return customers for given account IDs`() {
        // Given
        val accountIds = setOf("test-account-1", "test-account-2")

        // When
        val customers = customerRepository.findAllByAccountIdIn(accountIds)

        // Then
        assertEquals(2, customers.size)
    }

    @Test
    fun `existsCustomerByAccountId should return true when account ID exists`() {
        // Given
        val existingAccountId = "test-account-1"

        // When
        val exists = customerRepository.existsCustomerByAccountId(existingAccountId)

        // Then
        assertTrue(exists)
    }

    @Test
    fun `existsCustomerByAccountId should return false when account ID does not exist`() {
        // Given
        val nonExistingAccountId = "non-existing-account-id"

        // When
        val exists = customerRepository.existsCustomerByAccountId(nonExistingAccountId)

        // Then
        assertTrue(!exists)
    }

    @Test
    fun `delete customer should delete mortgage as well`() {
        // Given
        val customer = customerRepository.findByIdOrNull(customerId)

        // When
        customerRepository.delete(customer!!)

        // Then
        assertNull(mortgageRepository.findByIdOrNull(mortgageId))
    }
}