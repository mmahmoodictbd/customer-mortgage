package com.unloadbrain.customer.mortgage.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import org.hibernate.Hibernate
import java.util.UUID

@Entity
data class Customer(

    @Id
    val id: UUID,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val address: String,

    @Column(name = "account_id", nullable = false, unique = true)
    val accountId: String,

    @Column(nullable = false)
    val age: Int,

    @ManyToMany(mappedBy = "customers", cascade = [CascadeType.ALL])
    val mortgages: Set<Mortgage> = emptySet()

) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Customer

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "Customer(id=$id, name='$name', address='$address', accountId='$accountId', age=$age)"
    }
}