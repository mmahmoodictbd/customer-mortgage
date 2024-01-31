package com.unloadbrain.customer.mortgage.service

import com.unloadbrain.customer.mortgage.dto.response.AgifyResponse
import com.unloadbrain.customer.mortgage.exception.ServiceUnavailableException
import com.unloadbrain.customer.mortgage.exception.handler.GlobalExceptionHandler
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class AgifyApiService(@Qualifier("agifyApiRestTemplate") private val agifyApiRestTemplate: RestTemplate) {

    @CircuitBreaker(name = "agifyCircuitBreaker")
    fun getAgifyResponse(name: String): AgifyResponse =
        runCatching {
            agifyApiRestTemplate.getForObject(buildUri(name), AgifyResponse::class.java)
        }.onFailure {
            log.error("Could not get age from agify.io", it)
            throw ServiceUnavailableException("Could not get age from agify.io", it)
        }.getOrThrow()
            ?: error("Body for agify.io response cannot be null")

    private fun buildUri(name: String): URI = UriComponentsBuilder.fromUriString(AGIFY_ENDPOINT)
        .queryParam("name", name)
        .build()
        .toUri()

    companion object {
        private val log: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
        private const val AGIFY_ENDPOINT = "https://api.agify.io/"
    }
}
