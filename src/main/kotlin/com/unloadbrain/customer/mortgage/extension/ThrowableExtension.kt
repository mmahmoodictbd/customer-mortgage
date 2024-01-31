package com.unloadbrain.customer.mortgage.extension

import com.unloadbrain.customer.mortgage.constants.DEFAULT_ERROR_MESSAGE
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

fun Throwable.toProblemDetail(status: HttpStatus) =
    ProblemDetail.forStatusAndDetail(status, message ?: DEFAULT_ERROR_MESSAGE)
