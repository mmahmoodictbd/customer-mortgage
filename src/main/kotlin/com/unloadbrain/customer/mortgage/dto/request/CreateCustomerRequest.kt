package com.unloadbrain.customer.mortgage.dto.request

import jakarta.validation.constraints.NotBlank

data class CreateCustomerRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val address: String,
    @field:NotBlank val accountId: String
)