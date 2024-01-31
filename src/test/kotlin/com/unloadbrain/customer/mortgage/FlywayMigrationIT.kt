package com.unloadbrain.customer.mortgage

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FlywayMigrationIT(@Autowired private val flyway: Flyway) {

    @Test
    fun `verify Flyway migration is a success`() {
        assertTrue(flyway.validateWithResult().validationSuccessful)
        assertTrue(flyway.validateWithResult().invalidMigrations.isEmpty())
        assertTrue(flyway.validateWithResult().warnings.isEmpty())
    }

    @Test
    fun `verify total number of Flyway migration is 1`() {
        assertEquals(1, flyway.validateWithResult().validateCount)
    }
}
