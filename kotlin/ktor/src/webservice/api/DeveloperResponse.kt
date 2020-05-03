package com.rafaelcam.webservice.api

import java.time.Instant
import java.util.*

data class DeveloperResponse(
        val id: UUID,
        val name: String,
        val age: Int,
        val expertise: String,
        val createdAt: Instant
)