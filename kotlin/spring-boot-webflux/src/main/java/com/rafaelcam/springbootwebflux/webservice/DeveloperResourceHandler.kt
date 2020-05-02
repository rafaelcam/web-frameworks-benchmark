package com.rafaelcam.springbootwebflux.webservice

import com.rafaelcam.springbootwebflux.model.Developer
import com.rafaelcam.springbootwebflux.service.DeveloperService
import com.rafaelcam.springbootwebflux.webservice.api.DeveloperRequest
import com.rafaelcam.springbootwebflux.webservice.api.DeveloperResponse
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.time.Instant
import java.util.*

@Component
class DeveloperResourceHandler(private val service: DeveloperService) {

    suspend fun getAll(request: ServerRequest): ServerResponse {
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

        return ok().bodyValueAndAwait(developersResponse)
    }

    suspend fun getById(request: ServerRequest): ServerResponse {
        val id = UUID.fromString(request.pathVariable("id"))
        val developer = service.getById(id)

        return ok().bodyValueAndAwait(DeveloperResponse(
                id = developer.id,
                name = developer.name,
                age = developer.age,
                expertise = developer.expertise,
                createdAt = developer.createdAt
        ))
    }

    suspend fun create(request: ServerRequest): ServerResponse {
        val body = request.awaitBody<DeveloperRequest>()
        val developer = Developer(
                id = UUID.randomUUID(),
                name = body.name,
                age = body.age,
                expertise = body.expertise,
                createdAt = Instant.now()
        )

        val developerCreated = service.create(developer)
        return ok().bodyValueAndAwait(DeveloperResponse(
                id = developerCreated.id,
                name = developerCreated.name,
                age = developerCreated.age,
                expertise = developerCreated.expertise,
                createdAt = developerCreated.createdAt
        ))
    }

    suspend fun remove(request: ServerRequest): ServerResponse {
        val id = UUID.fromString(request.pathVariable("id"))
        service.remove(id)

        return ok().buildAndAwait()
    }
}