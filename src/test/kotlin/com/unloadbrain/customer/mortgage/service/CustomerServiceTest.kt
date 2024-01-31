package com.unloadbrain.customer.mortgage.service

import com.unloadbrain.customer.mortgage.dto.request.CreateCustomerRequest
import com.unloadbrain.customer.mortgage.dto.response.AgifyResponse
import com.unloadbrain.customer.mortgage.dto.response.CustomerResponse
import com.unloadbrain.customer.mortgage.dto.response.IdentityResponse
import com.unloadbrain.customer.mortgage.entity.Customer
import com.unloadbrain.customer.mortgage.exception.AccountIdAlreadyExistException
import com.unloadbrain.customer.mortgage.exception.ResourceNotFoundException
import com.unloadbrain.customer.mortgage.extension.toCustomerResponse
import com.unloadbrain.customer.mortgage.repository.CustomerRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID.randomUUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.mockito.Mockito.`when` as mockWhen

@ExtendWith(MockitoExtension::class)
class CustomerServiceTest {

    @Mock
    private lateinit var agifyApiService: AgifyApiService

    @Mock
    private lateinit var uuidService: UuidService

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @InjectMocks
    private lateinit var customerService: CustomerService

    @Test
    fun `should get all customers`() {
        // Given
        val pageNumber = 0
        val pageSize = 10
        val expectedCustomers = listOf(
            Customer(randomUUID(), "John", "Address1", "accountId1", 30).apply {
                createdAt = LocalDateTime.now()
                updatedAt = LocalDateTime.now()
            },
            Customer(randomUUID(), "Jane", "Address2", "accountId2", 25).apply {
                createdAt = LocalDateTime.now()
                updatedAt = LocalDateTime.now()
            },
        )
        val expectedCustomerResponses = expectedCustomers.map { it.toCustomerResponse() }

        mockWhen(customerRepository.findAll(PageRequest.of(pageNumber, pageSize)))
            .thenReturn(PageImpl(expectedCustomers))

        // When
        val result: Page<CustomerResponse> = customerService.getAllCustomers(pageNumber, pageSize)

        // Then
        assertEquals(expectedCustomerResponses, result.content)
    }

    @Test
    fun `should get customer by ID`() {
        // Given
        val customerId = randomUUID()
        val expectedCustomer = Customer(customerId, "John", "Address", "accountId", 30).apply {
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
        val expectedCustomerResponse = expectedCustomer.toCustomerResponse()

        mockWhen(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer))

        // When
        val result: CustomerResponse = customerService.getCustomerById(customerId)

        // Then
        assertEquals(expectedCustomerResponse, result)
    }

    @Test
    fun `should get customer by account ID`() {
        // Given
        val accountId = "accountId"
        val expectedCustomer = Customer(randomUUID(), "John", "Address", accountId, 30).apply {
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
        }
        val expectedCustomerResponse = expectedCustomer.toCustomerResponse()

        mockWhen(customerRepository.findByAccountId(accountId)).thenReturn(expectedCustomer)

        // When
        val result: CustomerResponse = customerService.getCustomerByAccountId(accountId)

        // Then
        assertEquals(expectedCustomerResponse, result)
    }

    @Test
    fun `should create customer`() {
        // Given
        val request = CreateCustomerRequest("John", "Address", "accountId")
        val expectedId = randomUUID()
        val expectedAge = 30
        val expectedIdentityResponse = IdentityResponse(expectedId)

        mockWhen(uuidService.getRandomUuid()).thenReturn(expectedId)
        mockWhen(agifyApiService.getAgifyResponse(request.name))
            .thenReturn(AgifyResponse(expectedAge, request.name, 80))
        mockWhen(customerRepository.existsCustomerByAccountId(request.accountId)).thenReturn(false)

        // When
        val result: IdentityResponse = customerService.createCustomer(request)

        // Then
        assertEquals(expectedIdentityResponse, result)
    }

    @Test
    fun `should throw AccountIdAlreadyExistException when creating customer with existing account ID`() {
        // Given
        val request = CreateCustomerRequest("John", "Address", "existingAccountId")

        mockWhen(customerRepository.existsCustomerByAccountId(request.accountId)).thenReturn(true)

        // When/Then
        assertFailsWith<AccountIdAlreadyExistException> {
            customerService.createCustomer(request)
        }
    }

    @Test
    fun `should throw ResourceNotFoundException when getting non-existing customer by ID`() {
        // Given
        val customerId = randomUUID()

        mockWhen(customerRepository.findById(customerId)).thenReturn(Optional.empty())

        // When/Then
        assertFailsWith<ResourceNotFoundException> {
            customerService.getCustomerById(customerId)
        }
    }

    @Test
    fun `should throw ResourceNotFoundException when getting non-existing customer by account ID`() {
        // Given
        val accountId = "nonExistingAccountId"

        mockWhen(customerRepository.findByAccountId(accountId)).thenReturn(null)

        // When/Then
        assertFailsWith<ResourceNotFoundException> {
            customerService.getCustomerByAccountId(accountId)
        }
    }

    @Test
    fun `should delete customer`() {
        // Given
        val customerId = randomUUID()
        val expectedCustomer = Customer(customerId, "John", "Address", "accountId", 30)

        mockWhen(customerRepository.findById(customerId)).thenReturn(Optional.of(expectedCustomer))

        // When
        customerService.deleteCustomer(customerId)

        // Then: Verify that delete method is called on repository
        verify(customerRepository).delete(expectedCustomer)
    }
}
