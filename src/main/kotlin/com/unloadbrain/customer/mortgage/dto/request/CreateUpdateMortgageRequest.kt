package com.unloadbrain.customer.mortgage.dto.request

import com.unloadbrain.customer.mortgage.entity.Mortgage.MortgageType
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class CreateUpdateMortgageRequest(
    @field:NotNull @field:Positive val mortgageSum: BigDecimal,
    @field:NotNull val startDate: LocalDate,
    @field:NotNull val endDate: LocalDate,
    @field:NotNull @field:Positive val interestPercentage: BigDecimal,
    @field:NotNull val mortgageType: MortgageType,
    @field:NotNull @field:Size(min = 1) val customerAccountIds: Set<String>,
)


