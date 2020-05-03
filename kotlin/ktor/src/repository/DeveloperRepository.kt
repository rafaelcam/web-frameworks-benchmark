package com.rafaelcam.repository

import com.rafaelcam.model.Developer
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.Instant
import java.util.*

object Developers : Table("developer") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val age = integer("age")
    val expertise = varchar("expertise", 255)
    val createdAt = datetime("created_at")
}

private fun toDeveloper(row: ResultRow): Developer {
    return Developer(
        id = row[Developers.id],
        name = row[Developers.name],
        age = row[Developers.age],
        expertise = row[Developers.expertise],
        createdAt = Instant.ofEpochMilli(row[Developers.createdAt].millis)
    )
}

class DeveloperRepository {
    fun findAll(): List<Developer> {
        return Developers.selectAll().map { toDeveloper(it) }
    }

    fun findById(id: UUID): Developer? {
        return Developers.select { Developers.id eq id }
            .map { toDeveloper(it) }
            .firstOrNull()
    }

    fun create(developer: Developer) {
        Developers.insert {
            it[id] = developer.id
            it[name] = developer.name
            it[age] = developer.age
            it[expertise] = developer.expertise
            it[createdAt] = DateTime(developer.createdAt.toEpochMilli())
        }
    }

    fun deleteById(id: UUID) {
        Developers.deleteWhere { Developers.id eq id }
    }
}