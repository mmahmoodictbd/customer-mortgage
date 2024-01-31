package com.unloadbrain.customer.mortgage.exception

data class ServiceUnavailableException(
    override val message: String,
    override val cause: Throwable? = null,
) : IntegrationException(message, cause)