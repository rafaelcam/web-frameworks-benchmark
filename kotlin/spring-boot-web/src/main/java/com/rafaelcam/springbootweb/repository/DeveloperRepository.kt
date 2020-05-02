package com.rafaelcam.springbootweb.repository

import com.rafaelcam.springbootweb.model.Developer
import com.rafaelcam.springbootweb.repository.query.DeveloperSQLExpressions
import com.rafaelcam.springbootweb.repository.rowmapper.DeveloperRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
class DeveloperRepository(private val jdbcTemplate: JdbcTemplate) {

    private val namedTemplate by lazy {
        NamedParameterJdbcTemplate(jdbcTemplate)
    }

    fun findAll(): List<Developer> {
        return try {
            namedTemplate.query(DeveloperSQLExpressions.SELECT_ALL, DeveloperRowMapper())
        } catch (ex: Exception) {
            emptyList()
        }
    }

    fun findById(id: UUID): Developer? {
        return try {
            val params = mapOf("id" to id)

            namedTemplate.queryForObject(DeveloperSQLExpressions.SELECT_BY_ID, params, DeveloperRowMapper())
        } catch (ex: Exception) {
            null
        }
    }

    fun create(developer: Developer) {
        val params = mapOf(
                "id" to developer.id,
                "name" to developer.name,
                "age" to developer.age,
                "expertise" to developer.expertise,
                "created_at" to Timestamp.from(developer.createdAt)
        )

        namedTemplate.update(DeveloperSQLExpressions.INSERT, params)
    }

    fun deleteById(id: UUID) {
        val params = mapOf(
                "id" to id
        )

        namedTemplate.update(DeveloperSQLExpressions.DELETE_BY_ID, params)
    }
}