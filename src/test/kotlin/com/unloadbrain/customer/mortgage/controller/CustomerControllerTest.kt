package com.unloadbrain.customer.mortgage.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.unloadbrain.customer.mortgage.dto.request.CreateCustomerRequest
import com.unloadbrain.customer.mortgage.dto.response.CustomerResponse
import com.unloadbrain.customer.mortgage.dto.response.IdentityResponse
import com.unloadbrain.customer.mortgage.service.CustomerService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID.randomUUID

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockBean private val customerService: CustomerService
) {

    @Test
    fun `should return all customers`() {
        // Given
        val pageNumber = 0
        val pageSize = 10
        val customerResponse = CustomerResponse(randomUUID(), "John Doe", "Amsterdam", "john-doe", 25)

        `when`(customerService.getAllCustomers(pageNumber, pageSize))
            .thenReturn(PageImpl(listOf(customerResponse), PageRequest.of(pageNumber, pageSize), 1))

        // When
        val resultActions: ResultActions = mockMvc.perform(get(API_CUSTOMERS_PATH))
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))

        // Then
        resultActions.andExpect(jsonPath("$.content[0].name").value("John Doe"))
            .andExpect(jsonPath("$.content[0].address").value("Amsterdam"))
            .andExpect(jsonPath("$.content[0].accountId").value("john-doe"))
            .andExpect(jsonPath("$.content[0].age").value(25))
    }

    @Test
    fun `should return customer by ID`() {
        // Given
        val customerId = randomUUID()
        val customerResponse = CustomerResponse(customerId, "John Doe", "Amsterdam", "john-doe", 25)

        `when`(customerService.getCustomerById(customerId)).thenReturn(customerResponse)

        // When
        val resultActions: ResultActions = mockMvc.perform(get("$API_CUSTOMERS_PATH/{id}", customerId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))

        // Then
        resultActions.andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.address").value("Amsterdam"))
            .andExpect(jsonPath("$.accountId").value("john-doe"))
            .andExpect(jsonPath("$.age").value(25))
    }

    @Test
    fun `should return customer by account ID`() {
        // Given
        val accountId = "john-doe"
        val customerResponse = CustomerResponse(randomUUID(), "John Doe", "Amsterdam", accountId, 25)

        `when`(customerService.getCustomerByAccountId(accountId)).thenReturn(customerResponse)

        // When
        val resultActions: ResultActions = mockMvc.perform(get("$API_CUSTOMERS_PATH/accounts/{accountId}", accountId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))

        // Then
        resultActions.andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.address").value("Amsterdam"))
            .andExpect(jsonPath("$.accountId").value(accountId))
            .andExpect(jsonPath("$.age").value(25))
    }

    @Test
    fun `should create customer`() {
        // Given
        val createCustomerRequest = CreateCustomerRequest("John Doe", "Amsterdam", "john-doe")

        val customerId = randomUUID()
        val identityResponse = IdentityResponse(customerId)

        `when`(customerService.createCustomer(createCustomerRequest)).thenReturn(identityResponse)

        // When
        val resultActions: ResultActions = mockMvc.perform(
            post(API_CUSTOMERS_PATH)
                .content(objectMapper.writeValueAsString(createCustomerRequest))
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(APPLICATION_JSON))

        // Then
        resultActions.andExpect(jsonPath("$.id").value(customerId.toString()))
    }

    @Test
    fun `should delete customer`() {
        // Given
        val customerId = randomUUID()

        // When
        val resultActions: ResultActions = mockMvc.perform(delete("$API_CUSTOMERS_PATH/{id}", customerId))
            .andExpect(status().isNoContent)

        // Then
        resultActions.andExpect(status().isNoContent)
    }

    companion object {
        const val API_CUSTOMERS_PATH = "/api/customers"
    }
}
