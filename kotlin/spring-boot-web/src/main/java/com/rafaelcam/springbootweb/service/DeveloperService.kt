package com.rafaelcam.springbootweb.service

import com.rafaelcam.springbootweb.model.Developer
import com.rafaelcam.springbootweb.repository.DeveloperRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class DeveloperService(private val repository: DeveloperRepository) {

    fun getAll(): List<Developer> {
        return repository.findAll()
    }

    fun getById(id: UUID): Developer {
        return repository.findById(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    fun create(developer: Developer): Developer {
        repository.create(developer)
        return developer
    }

    fun remove(id: UUID) {
        repository.deleteById(id)
    }
}