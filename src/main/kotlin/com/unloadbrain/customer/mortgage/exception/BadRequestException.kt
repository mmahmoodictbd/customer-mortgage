package com.unloadbrain.customer.mortgage.exception

abstract class BadRequestException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)