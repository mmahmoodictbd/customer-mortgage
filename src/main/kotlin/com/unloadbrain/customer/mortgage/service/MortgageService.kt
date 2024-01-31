package com.unloadbrain.customer.mortgage.service

import com.unloadbrain.customer.mortgage.dto.request.CreateUpdateMortgageRequest
import com.unloadbrain.customer.mortgage.dto.response.IdentityResponse
import com.unloadbrain.customer.mortgage.dto.response.MortgageResponse
import com.unloadbrain.customer.mortgage.entity.Customer
import com.unloadbrain.customer.mortgage.entity.Mortgage
import com.unloadbrain.customer.mortgage.exception.ResourceNotFoundException
import com.unloadbrain.customer.mortgage.extension.toMortgageResponse
import com.unloadbrain.customer.mortgage.repository.CustomerRepository
import com.unloadbrain.customer.mortgage.repository.MortgageRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MortgageService(
    private val uuidService: UuidService,
    private val mortgageRepository: MortgageRepository,
    private val customerRepository: CustomerRepository
) {

    fun getMortgages(pageNumber: Int, pageSize: Int): Page<MortgageResponse> {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        val mortgagePage = mortgageRepository.findAll(pageRequest)
        println(mortgagePage.content)
        return mortgagePage.map { it.toMortgageResponse() }
    }

    fun getMortgageById(id: UUID): MortgageResponse {
        return getMortgage(id).toMortgageResponse()
    }

    fun createMortgage(request: CreateUpdateMortgageRequest): IdentityResponse {
        validateCustomerAccountsExist(request.customerAccountIds)

        val id = uuidService.getRandomUuid()
        mortgageRepository.saveAndFlush(
            Mortgage(
                id = id,
                mortgageSum = request.mortgageSum,
                startDate = request.startDate,
                endDate = request.endDate,
                interestPercentage = request.interestPercentage,
                mortgageType = request.mortgageType,
                customers = getCustomersByAccountIds(request.customerAccountIds)
            )
        )
        return IdentityResponse(id)
    }

    fun updateMortgage(id: UUID, request: CreateUpdateMortgageRequest) {
        validateCustomerAccountsExist(request.customerAccountIds)

        val existingMortgage = getMortgage(id)
        mortgageRepository.save(
            existingMortgage.copy(
                mortgageSum = request.mortgageSum,
                startDate = request.startDate,
                endDate = request.endDate,
                interestPercentage = request.interestPercentage,
                mortgageType = request.mortgageType,
                customers = getCustomersByAccountIds(request.customerAccountIds)
            )
        )
    }

    fun deleteMortgage(id: UUID) {
        getMortgage(id).also { mortgageRepository.delete(it) }
    }

    private fun validateCustomerAccountsExist(customerAccountIds: Set<String>) {
        val nonExistingAccountIds = customerAccountIds.filterNot {
            customerRepository.existsCustomerByAccountId(it)
        }
        if (nonExistingAccountIds.isNotEmpty()) {
            throw ResourceNotFoundException("Customers with account Ids $nonExistingAccountIds do not exist.")
        }
    }

    private fun getCustomersByAccountIds(customerAccountIds: Set<String>): Set<Customer> {
        return customerRepository.findAllByAccountIdIn(customerAccountIds).toSet()
    }

    private fun getMortgage(id: UUID): Mortgage {
        return mortgageRepository.findByIdOrNull(id)
            ?: throw ResourceNotFoundException("Mortgage with id $id not found.")
    }
}
