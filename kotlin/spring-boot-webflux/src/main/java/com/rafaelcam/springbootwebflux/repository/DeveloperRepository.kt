package com.rafaelcam.springbootwebflux.repository

import com.rafaelcam.springbootwebflux.model.Developer
import com.rafaelcam.springbootwebflux.repository.common.get
import com.rafaelcam.springbootwebflux.repository.query.DeveloperSQLExpressions
import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.await
import org.springframework.data.r2dbc.core.awaitOneOrNull
import org.springframework.data.r2dbc.core.flow
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class DeveloperRepository(private val db: DatabaseClient) {

    suspend fun findAll(): Flow<Developer> {
        return db.execute(DeveloperSQLExpressions.SELECT_ALL)
                .map { row, _ -> row.toDeveloper() }
                .flow()
    }

    suspend fun findById(id: UUID): Developer? {
        return db.execute(DeveloperSQLExpressions.SELECT_BY_ID)
                .bind("id", id)
                .map { row, _ -> row.toDeveloper() }
                .awaitOneOrNull()
    }

    suspend fun create(developer: Developer) {
        db.execute(DeveloperSQLExpressions.INSERT)
                .bind("id", developer.id)
                .bind("name", developer.name)
                .bind("age", developer.age)
                .bind("expertise", developer.expertise)
                .bind("created_at", developer.createdAt)
                .await()
    }

    suspend fun deleteById(id: UUID) {
        db.execute(DeveloperSQLExpressions.DELETE_BY_ID)
                .bind("id", id)
                .await()
    }

    private fun Row.toDeveloper() = Developer(
            id = this.get<UUID>("id"),
            name = this.get<String>("name"),
            age = this.get<Int>("age"),
            expertise = this.get<String>("expertise"),
            createdAt = this.get<Instant>("created_at")
    )
}