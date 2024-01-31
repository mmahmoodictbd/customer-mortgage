package com.unloadbrain.customer.mortgage.controller

import com.unloadbrain.customer.mortgage.dto.request.CreateCustomerRequest
import com.unloadbrain.customer.mortgage.dto.response.CustomerResponse
import com.unloadbrain.customer.mortgage.dto.response.IdentityResponse
import com.unloadbrain.customer.mortgage.service.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/customers")
class CustomerController(private val customerService: CustomerService) {

    @GetMapping
    fun getAllCustomers(
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): Page<CustomerResponse> = customerService.getAllCustomers(pageNumber, pageSize)

    @GetMapping("/{id}")
    fun getCustomerById(@PathVariable id: UUID): CustomerResponse =
        customerService.getCustomerById(id)

    @GetMapping("/accounts/{accountId}")
    fun getCustomerByAccountId(@PathVariable accountId: String): CustomerResponse =
        customerService.getCustomerByAccountId(accountId)

    @PostMapping
    @ResponseStatus(CREATED)
    fun createCustomer(@Valid @RequestBody request: CreateCustomerRequest): IdentityResponse =
        customerService.createCustomer(request)

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun deleteCustomer(@PathVariable id: UUID) = customerService.deleteCustomer(id)

}