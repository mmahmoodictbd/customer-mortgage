package com.unloadbrain.customer.mortgage.service

import com.unloadbrain.customer.mortgage.dto.request.CreateCustomerRequest
import com.unloadbrain.customer.mortgage.dto.response.CustomerResponse
import com.unloadbrain.customer.mortgage.dto.response.IdentityResponse
import com.unloadbrain.customer.mortgage.entity.Customer
import com.unloadbrain.customer.mortgage.exception.AccountIdAlreadyExistException
import com.unloadbrain.customer.mortgage.exception.ResourceNotFoundException
import com.unloadbrain.customer.mortgage.extension.toCustomerResponse
import com.unloadbrain.customer.mortgage.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerService(
    private val agifyApiService: AgifyApiService,
    private val uuidService: UuidService,
    private val customerRepository: CustomerRepository
) {

    fun getAllCustomers(pageNumber: Int, pageSize: Int): Page<CustomerResponse> {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        return customerRepository.findAll(pageRequest).map { it.toCustomerResponse() }
    }

    fun getCustomerById(id: UUID): CustomerResponse {
        return getCustomer(id).toCustomerResponse()
    }

    fun getCustomerByAccountId(accountId: String): CustomerResponse {
        return getCustomer(accountId).toCustomerResponse()
    }

    fun createCustomer(request: CreateCustomerRequest): IdentityResponse {
        validateAccountId(request.accountId)
        val id = uuidService.getRandomUuid()
        customerRepository.saveAndFlush(
            Customer(
                id = id,
                name = request.name,
                address = request.address,
                accountId = request.accountId,
                age = agifyApiService.getAgifyResponse(request.name).age
            )
        )
        return IdentityResponse(id)
    }

    fun deleteCustomer(id: UUID) {
        getCustomer(id).also {
            customerRepository.delete(it)
        }
    }

    private fun getCustomer(id: UUID): Customer {
        return customerRepository.findByIdOrNull(id)
            ?: throw ResourceNotFoundException("Customer with id $id not found.")
    }

    private fun getCustomer(accountId: String): Customer {
        return customerRepository.findByAccountId(accountId)
            ?: throw ResourceNotFoundException("Customer with accountId $accountId not found.")
    }

    private fun validateAccountId(accountId: String) {
        if (customerRepository.existsCustomerByAccountId(accountId)) {
            throw AccountIdAlreadyExistException("AccountId $accountId already exists.")
        }
    }
}
