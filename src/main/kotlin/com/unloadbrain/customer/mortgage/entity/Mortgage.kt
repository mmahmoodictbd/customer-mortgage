package com.unloadbrain.customer.mortgage.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import org.hibernate.Hibernate
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
data class Mortgage(

    @Id
    val id: UUID,

    @Column(nullable = false)
    val mortgageSum: BigDecimal,

    @Column(nullable = false)
    val startDate: LocalDate,

    @Column(nullable = false)
    val endDate: LocalDate,

    @Column(nullable = false)
    val interestPercentage: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val mortgageType: MortgageType,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "mortgage_customer",
        joinColumns = [JoinColumn(name = "mortgage_id")],
        inverseJoinColumns = [JoinColumn(name = "customer_id")]
    )
    val customers: Set<Customer> = emptySet(),

    ) : BaseEntity() {

    enum class MortgageType {
        AFV, LIN, ANN
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Mortgage

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "Mortgage(id=$id, mortgageSum=$mortgageSum, startDate=$startDate, endDate=$endDate, " +
                "interestPercentage=$interestPercentage, mortgageType=$mortgageType"
    }
}


