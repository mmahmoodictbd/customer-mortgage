package com.unloadbrain.customer.mortgage.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration


@Configuration
class AppConfig(val restTemplateBuilder: RestTemplateBuilder) {

    @Bean
    fun agifyApiRestTemplate(): RestTemplate =
        restTemplateBuilder
            .setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT_IN_MILLIS))
            .setReadTimeout(Duration.ofMillis(READ_TIMEOUT_IN_MILLIS))
            .build()

    companion object {
        private const val CONNECT_TIMEOUT_IN_MILLIS = 3000L
        private const val READ_TIMEOUT_IN_MILLIS = 3000L
    }
}
