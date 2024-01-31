package com.unloadbrain.customer.mortgage.repository

import com.unloadbrain.customer.mortgage.entity.Mortgage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MortgageRepository : JpaRepository<Mortgage, UUID>