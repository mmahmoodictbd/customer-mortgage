package com.unloadbrain.customer.mortgage

import com.unloadbrain.customer.mortgage.dto.response.AgifyResponse
import com.unloadbrain.customer.mortgage.exception.ServiceUnavailableException
import com.unloadbrain.customer.mortgage.service.AgifyApiService
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State.CLOSED
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import java.net.URI

@SpringBootTest
@ActiveProfiles("circuit-breaker-test")
class CircuitBreakerIT {

    @Autowired
    private lateinit var agifyApiService: AgifyApiService

    @MockBean(name = "agifyApiRestTemplate")
    private lateinit var agifyApiRestTemplate: RestTemplate

    @Autowired
    private lateinit var circuitBreakerRegistry: CircuitBreakerRegistry

    @BeforeEach
    fun setup() {
        circuitBreakerRegistry.circuitBreaker(AGIFY_CB_NAME).reset()
    }

    @Test
    fun `getAgifyResponse - circuitBreakers should be closed if there is no failure call`() {
        // Given
        whenever(
            agifyApiRestTemplate.getForObject(any<URI>(), any<Class<*>>())
        ).thenReturn(AgifyResponse(1, "Mossaddeque Mahmood", 36))

        // When
        repeat(3) {
            agifyApiService.getAgifyResponse("Mossaddeque Mahmood")
        }

        // Then
        val state = circuitBreakerRegistry.circuitBreaker(AGIFY_CB_NAME).state
        assertThat(state).isEqualTo(CLOSED)
    }

    @Test
    fun `getAgifyResponse - circuitBreakers should be open after second failure call as configured`() {
        // Given
        whenever(
            agifyApiRestTemplate.getForObject(any<URI>(), any<Class<*>>())
        ).thenThrow(ServiceUnavailableException("Failed"))

        // When
        val exceptions = List(3) {
            catchThrowable { agifyApiService.getAgifyResponse("Mossaddeque Mahmood") }
        }

        // Then
        val state = circuitBreakerRegistry.circuitBreaker(AGIFY_CB_NAME).state
        assertThat(state).isEqualTo(OPEN)
        assertThat(exceptions.filterIsInstance<ServiceUnavailableException>().size).isEqualTo(2)
        assertThat(exceptions.filterIsInstance<CallNotPermittedException>().size).isEqualTo(1)
    }

    companion object {
        const val AGIFY_CB_NAME = "agifyCircuitBreaker"
    }
}
