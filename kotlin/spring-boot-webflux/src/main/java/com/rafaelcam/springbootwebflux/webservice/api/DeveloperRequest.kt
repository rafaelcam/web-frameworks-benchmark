package com.rafaelcam.springbootwebflux.webservice.api

data class DeveloperRequest(
        val name: String,
        val age: Int,
        val expertise: String
)