package com.unloadbrain.customer.mortgage.exception

abstract class IntegrationException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)