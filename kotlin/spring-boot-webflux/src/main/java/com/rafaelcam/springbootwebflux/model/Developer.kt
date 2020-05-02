package com.rafaelcam.springbootwebflux.model

import org.springframework.data.annotation.Id
import java.time.Instant
import java.util.*

data class Developer(
        @Id
        val id: UUID,
        val name: String,
        val age: Int,
        val expertise: String,
        val createdAt: Instant
)