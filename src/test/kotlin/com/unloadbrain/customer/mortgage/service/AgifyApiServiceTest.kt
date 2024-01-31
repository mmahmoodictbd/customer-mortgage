package com.unloadbrain.customer.mortgage.service

import com.unloadbrain.customer.mortgage.dto.response.AgifyResponse
import com.unloadbrain.customer.mortgage.exception.ServiceUnavailableException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import kotlin.test.assertFailsWith
import org.mockito.Mockito.`when` as mockWhen

@ExtendWith(MockitoExtension::class)
class AgifyApiServiceTest {

    @Mock
    private lateinit var agifyApiRestTemplate: RestTemplate

    @Test
    fun `should get Agify response successfully`() {
        // Given
        val name = "John"
        val expectedAgifyResponse = AgifyResponse(30, name, 80)

        mockWhen(
            agifyApiRestTemplate.getForObject(buildExpectedUri(name), AgifyResponse::class.java)
        ).thenReturn(expectedAgifyResponse)

        val agifyApiService = AgifyApiService(agifyApiRestTemplate)

        // When
        val agifyResponse = agifyApiService.getAgifyResponse(name)

        // Then
        assertNotNull(agifyResponse)
        assertEquals(expectedAgifyResponse, agifyResponse)
    }

    @Test
    fun `should handle service unavailable exception`() {
        // Given
        val name = "John"

        mockWhen(
            agifyApiRestTemplate.getForObject(buildExpectedUri(name), AgifyResponse::class.java)
        ).thenThrow(ServiceUnavailableException("Service Unavailable"))

        val agifyApiService = AgifyApiService(agifyApiRestTemplate)

        // When/Then
        val exception = assertFailsWith<ServiceUnavailableException> {
            agifyApiService.getAgifyResponse(name)
        }

        assertEquals("Could not get age from agify.io", exception.message)
    }

    @Test
    fun `should handle null response body`() {
        // Given
        val name = "John"

        mockWhen(
            agifyApiRestTemplate.getForObject(buildExpectedUri(name), AgifyResponse::class.java)
        ).thenReturn(null)

        val agifyApiService = AgifyApiService(agifyApiRestTemplate)

        // When/Then
        val exception = assertFailsWith<IllegalStateException> {
            agifyApiService.getAgifyResponse(name)
        }

        assertEquals("Body for agify.io response cannot be null", exception.message)
    }

    private fun buildExpectedUri(name: String): URI = UriComponentsBuilder.fromUriString(AGIFY_ENDPOINT)
        .queryParam("name", name)
        .build()
        .toUri()

    companion object {
        private const val AGIFY_ENDPOINT = "https://api.agify.io/"
    }
}
