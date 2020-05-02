package com.rafaelcam.springbootweb.model

import java.time.Instant
import java.util.*

data class Developer(
        val id: UUID,
        val name: String,
        val age: Int,
        val expertise: String,
        val createdAt: Instant
)