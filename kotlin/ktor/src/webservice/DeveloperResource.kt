package com.rafaelcam.webservice

import com.rafaelcam.model.Developer
import com.rafaelcam.service.DeveloperService
import com.rafaelcam.webservice.api.DeveloperRequest
import com.rafaelcam.webservice.api.DeveloperResponse
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.time.Instant
import java.util.*

class DeveloperResource

fun Routing.developerResource(service: DeveloperService = DeveloperService()) {

    route("api/v1/developers") {

        get {
            val developers = service.getAll()
            val developersResponse = developers.map {
                DeveloperResponse(
                    id = it.id,
                    name = it.name,
                    age = it.age,
                    expertise = it.expertise,
                    createdAt = it.createdAt
                )
            }

            call.respond(developersResponse)
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val developer = service.getById(id)

            call.respond(DeveloperResponse(
                id = developer.id,
                name = developer.name,
                age = developer.age,
                expertise = developer.expertise,
                createdAt = developer.createdAt
            ))
        }

        post {
            val request = call.receive<DeveloperRequest>()
            val developer = Developer(
                id = UUID.randomUUID(),
                name = request.name,
                age = request.age,
                expertise = request.expertise,
                createdAt = Instant.now()
            )

            val developerCreated = service.create(developer)

            call.respond(DeveloperResponse(
                id = developerCreated.id,
                name = developerCreated.name,
                age = developerCreated.age,
                expertise = developerCreated.expertise,
                createdAt = developerCreated.createdAt
            ))
        }

        delete("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            service.remove(id)

            call.respond(mapOf("success" to true))
        }
    }
}