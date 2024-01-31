package com.unloadbrain.customer.mortgage.exception

data class AccountIdAlreadyExistException(
    override val message: String,
    override val cause: Throwable? = null,
) : BadRequestException(message, cause)
