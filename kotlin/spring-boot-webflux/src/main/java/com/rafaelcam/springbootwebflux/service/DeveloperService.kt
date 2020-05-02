package com.rafaelcam.springbootwebflux.service

import com.rafaelcam.springbootwebflux.model.Developer
import com.rafaelcam.springbootwebflux.repository.DeveloperRepository
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class DeveloperService(private val repository: DeveloperRepository) {

    suspend fun getAll(): List<Developer> {
        return repository.findAll().toList()
    }

    suspend fun getById(id: UUID): Developer {
        return repository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    suspend fun create(developer: Developer): Developer {
        repository.create(developer)
        return developer
    }

    suspend fun remove(id: UUID) {
        repository.deleteById(id)
    }
}