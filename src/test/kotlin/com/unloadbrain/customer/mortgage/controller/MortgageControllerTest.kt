package com.unloadbrain.customer.mortgage.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.unloadbrain.customer.mortgage.dto.request.CreateUpdateMortgageRequest
import com.unloadbrain.customer.mortgage.dto.response.IdentityResponse
import com.unloadbrain.customer.mortgage.dto.response.MortgageResponse
import com.unloadbrain.customer.mortgage.entity.Mortgage.MortgageType
import com.unloadbrain.customer.mortgage.service.MortgageService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.util.UUID.randomUUID

@SpringBootTest
@AutoConfigureMockMvc
class MortgageControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    @MockBean private val mortgageService: MortgageService
) {

    companion object {
        const val API_MORTGAGES_PATH = "/api/mortgages"
    }

    @Test
    fun `should return all mortgages`() {
        // Given
        val pageNumber = 0
        val pageSize = 10
        val mortgageResponse = MortgageResponse(
            randomUUID(), 100000.toBigDecimal(), LocalDate.now(), LocalDate.now().plusYears(30),
            3.5.toBigDecimal(), MortgageType.ANN, setOf()
        )

        `when`(mortgageService.getMortgages(pageNumber, pageSize))
            .thenReturn(PageImpl(listOf(mortgageResponse), PageRequest.of(pageNumber, pageSize), 1))

        // When
        val resultActions: ResultActions = mockMvc.perform(get(API_MORTGAGES_PATH))
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))

        // Then
        resultActions.andExpect(jsonPath("$.content[0].mortgageSum").value(100000))
            .andExpect(jsonPath("$.content[0].interestPercentage").value(3.5))
    }

    @Test
    fun `should return mortgage by ID`() {
        // Given
        val mortgageId = randomUUID()
        val mortgageResponse = MortgageResponse(
            mortgageId, 100000.toBigDecimal(), LocalDate.now(), LocalDate.now().plusYears(30),
            3.5.toBigDecimal(), MortgageType.ANN, setOf()
        )

        `when`(mortgageService.getMortgageById(mortgageId)).thenReturn(mortgageResponse)

        // When
        val resultActions: ResultActions = mockMvc.perform(get("$API_MORTGAGES_PATH/{id}", mortgageId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))

        // Then
        resultActions.andExpect(jsonPath("$.mortgageSum").value(100000))
            .andExpect(jsonPath("$.interestPercentage").value(3.5))
    }

    @Test
    fun `should create mortgage`() {
        // Given
        val createUpdateMortgageRequest = CreateUpdateMortgageRequest(
            100000.toBigDecimal(), LocalDate.now(), LocalDate.now().plusYears(30),
            3.5.toBigDecimal(), MortgageType.ANN, setOf("test-account-1", "test-account-2")
        )

        val mortgageId = randomUUID()
        val identityResponse = IdentityResponse(mortgageId)

        `when`(mortgageService.createMortgage(createUpdateMortgageRequest)).thenReturn(identityResponse)

        // When
        val resultActions: ResultActions = mockMvc.perform(
            post(API_MORTGAGES_PATH)
                .content(objectMapper.writeValueAsString(createUpdateMortgageRequest))
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isCreated).andExpect(content().contentType(APPLICATION_JSON))

        // Then
        resultActions.andExpect(jsonPath("$.id").value(mortgageId.toString()))
    }

    @Test
    fun `should update mortgage`() {
        // Given
        val mortgageId = randomUUID()
        val createUpdateMortgageRequest = CreateUpdateMortgageRequest(
            100000.toBigDecimal(), LocalDate.now(), LocalDate.now().plusYears(30),
            3.5.toBigDecimal(), MortgageType.ANN, setOf("test-account-1", "test-account-2")
        )

        // When and Then
        val resultActions: ResultActions = mockMvc.perform(
            put("$API_MORTGAGES_PATH/{id}", mortgageId)
                .content(objectMapper.writeValueAsString(createUpdateMortgageRequest))
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
    }

    @Test
    fun `should delete mortgage`() {
        // Given
        val mortgageId = randomUUID()

        // When
        val resultActions: ResultActions = mockMvc.perform(delete("$API_MORTGAGES_PATH/{id}", mortgageId))
            .andExpect(status().isNoContent)

        // Then
        resultActions.andExpect(status().isNoContent)
    }
}
