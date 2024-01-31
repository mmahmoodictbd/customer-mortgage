package com.unloadbrain.customer.mortgage.service

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UuidService {

    fun getRandomUuid(): UUID = UUID.randomUUID()
}
