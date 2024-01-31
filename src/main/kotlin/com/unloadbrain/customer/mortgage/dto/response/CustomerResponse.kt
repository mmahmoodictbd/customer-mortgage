package com.unloadbrain.customer.mortgage.dto.response

import java.util.UUID

data class CustomerResponse(
    val id: UUID,
    val name: String,
    val address: String,
    val accountId: String,
    val age: Int
)