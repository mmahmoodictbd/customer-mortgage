package com.unloadbrain.customer.mortgage.extension

import com.unloadbrain.customer.mortgage.dto.response.CustomerResponse
import com.unloadbrain.customer.mortgage.entity.Customer
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


fun Customer.toCustomerResponse(): CustomerResponse {
    return CustomerResponse(
        id = id,
        name = name,
        address = address,
        accountId = accountId,
        age = calculateNewAge(age, createdAt)
    )
}

fun calculateNewAge(oldAge: Int, creationDateTime: LocalDateTime): Int {
    val currentDateTime = LocalDateTime.now()
    val yearsBetween = ChronoUnit.YEARS.between(creationDateTime, currentDateTime)
    return oldAge + yearsBetween.toInt()
}