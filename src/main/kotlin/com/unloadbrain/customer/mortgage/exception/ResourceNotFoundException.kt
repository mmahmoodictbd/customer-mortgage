package com.unloadbrain.customer.mortgage.exception

data class ResourceNotFoundException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)