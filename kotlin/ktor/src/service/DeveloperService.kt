package com.rafaelcam.service

import com.rafaelcam.model.Developer
import com.rafaelcam.repository.DeveloperRepository
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.IllegalInstantException
import java.util.*

class DeveloperService(private val repository: DeveloperRepository = DeveloperRepository()) {

    fun getAll(): List<Developer> {
        return transaction {
            return@transaction repository.findAll()
        }
    }

    fun getById(id: UUID): Developer {
        return transaction {
            return@transaction repository.findById(id) ?:
                throw IllegalInstantException("Developer not found")
        }
    }

    fun create(developer: Developer): Developer {
        transaction {
            repository.create(developer)
        }

        return developer
    }

    fun remove(id: UUID) {
        transaction {
            repository.deleteById(id)
        }
    }
}