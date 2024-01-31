package com.unloadbrain.customer.mortgage.controller

import com.unloadbrain.customer.mortgage.dto.request.CreateUpdateMortgageRequest
import com.unloadbrain.customer.mortgage.dto.response.IdentityResponse
import com.unloadbrain.customer.mortgage.dto.response.MortgageResponse
import com.unloadbrain.customer.mortgage.service.MortgageService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/mortgages")
class MortgageController(private val mortgageService: MortgageService) {

    @GetMapping
    fun getMortgages(
        @RequestParam(defaultValue = "0") pageNumber: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): Page<MortgageResponse> =
        mortgageService.getMortgages(pageNumber, pageSize)

    @GetMapping("/{id}")
    fun getMortgageById(@PathVariable id: UUID): MortgageResponse = mortgageService.getMortgageById(id)

    @PostMapping
    @ResponseStatus(CREATED)
    fun createMortgage(@RequestBody @Valid request: CreateUpdateMortgageRequest): IdentityResponse =
        mortgageService.createMortgage(request)

    @PutMapping("/{id}")
    fun updateMortgage(
        @PathVariable id: UUID, @RequestBody @Valid request: CreateUpdateMortgageRequest
    ) = mortgageService.updateMortgage(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    fun deleteMortgage(@PathVariable id: UUID) = mortgageService.deleteMortgage(id)

}
