package com.unloadbrain.customer.mortgage.repository

import com.unloadbrain.customer.mortgage.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomerRepository : JpaRepository<Customer, UUID> {

    fun findByAccountId(accountId: String): Customer?

    fun findAllByAccountIdIn(accountIds: Set<String>) : List<Customer>

    fun existsCustomerByAccountId(accountId: String): Boolean
}