package com.unloadbrain.customer.mortgage.extension

import com.unloadbrain.customer.mortgage.dto.response.MortgageResponse
import com.unloadbrain.customer.mortgage.dto.response.MortgageResponse.Customer
import com.unloadbrain.customer.mortgage.entity.Mortgage

fun Mortgage.toMortgageResponse(): MortgageResponse {
    return MortgageResponse(
        id = id,
        mortgageSum = mortgageSum,
        startDate = startDate,
        endDate = endDate,
        interestPercentage = interestPercentage,
        mortgageType = mortgageType,
        customers = customers.map { customer ->
            Customer(
                customer.id,
                customer.name,
                customer.accountId
            )
        }.toSet()
    )
}
