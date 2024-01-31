package com.unloadbrain.customer.mortgage.exception

data class AgifyUnknownException(
    override val message: String,
    override val cause: Throwable? = null,
) : IntegrationException(message, cause)
