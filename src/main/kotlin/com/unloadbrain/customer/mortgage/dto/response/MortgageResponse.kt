package com.unloadbrain.customer.mortgage.dto.response

import com.unloadbrain.customer.mortgage.entity.Mortgage.MortgageType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class MortgageResponse(
    val id: UUID,
    val mortgageSum: BigDecimal,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val interestPercentage: BigDecimal,
    val mortgageType: MortgageType,
    val customers: Set<Customer>
) {
    data class Customer(
        val id: UUID,
        val name: String,
        val accountId: String
    )
}
