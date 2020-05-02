package com.rafaelcam.springbootweb.webservice

import com.rafaelcam.springbootweb.model.Developer
import com.rafaelcam.springbootweb.service.DeveloperService
import com.rafaelcam.springbootweb.webservice.api.DeveloperRequest
import com.rafaelcam.springbootweb.webservice.api.DeveloperResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping(value = ["/api/v1/developers"])
class DeveloperResource(private val service: DeveloperService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAll(): ResponseEntity<List<DeveloperResponse>> {
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

        return ok(developersResponse)
    }

    @GetMapping(value = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getById(@PathVariable id: UUID): ResponseEntity<DeveloperResponse> {
        val developer = service.getById(id)

        return ok(DeveloperResponse(
                id = developer.id,
                name = developer.name,
                age = developer.age,
                expertise = developer.expertise,
                createdAt = developer.createdAt
        ))
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun create(@RequestBody request: DeveloperRequest): ResponseEntity<DeveloperResponse> {
        val developer = Developer(
                id = UUID.randomUUID(),
                name = request.name,
                age = request.age,
                expertise = request.expertise,
                createdAt = Instant.now()
        )

        val developerCreated = service.create(developer)
        return ok(DeveloperResponse(
                id = developerCreated.id,
                name = developerCreated.name,
                age = developerCreated.age,
                expertise = developerCreated.expertise,
                createdAt = developerCreated.createdAt
        ))
    }

    @DeleteMapping(value = ["/{id}"])
    fun remove(@PathVariable id: UUID): ResponseEntity<Void> {
        service.remove(id)
        return ok().build()
    }
}