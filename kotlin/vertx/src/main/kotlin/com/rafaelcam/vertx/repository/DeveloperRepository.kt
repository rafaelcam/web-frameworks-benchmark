package com.rafaelcam.vertx.repository

import com.rafaelcam.vertx.errorcode.ERROR_ON_CREATING_A_DEVELOPER
import com.rafaelcam.vertx.errorcode.ERROR_ON_REMOVING_A_DEVELOPER
import com.rafaelcam.vertx.errorcode.ERROR_ON_RETRIEVING_ALL_DEVELOPERS
import com.rafaelcam.vertx.errorcode.ERROR_ON_RETRIEVING_ONE_DEVELOPER
import com.rafaelcam.vertx.repository.query.DeveloperSQLExpressions
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.pgclient.pgConnectOptionsOf
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.kotlin.sqlclient.preparedQueryAwait
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class DeveloperRepository : CoroutineVerticle() {

    private val connection: PgPool by lazy {
        val databaseConfig: JsonObject = config.getJsonObject("database")
        PgPool.pool(
            vertx, pgConnectOptionsOf(
            host = databaseConfig.getString("host"),
            port = databaseConfig.getInteger("port"),
            database = databaseConfig.getString("database"),
            user = databaseConfig.getString("username"),
            password = databaseConfig.getString("password")
        ).addProperty("search_path", databaseConfig.getString("schema")),
            poolOptionsOf(
                maxSize = databaseConfig.getInteger("connection_pool_size")
            )
        )
    }

    override suspend fun start() {
        vertx.eventBus().consumer(DEVELOPER_REPOSITORY_GET_ALL, getAll)
        vertx.eventBus().consumer(DEVELOPER_REPOSITORY_GET_BY_ID, getById)
        vertx.eventBus().consumer(DEVELOPER_REPOSITORY_CREATE, create)
        vertx.eventBus().consumer(DEVELOPER_REPOSITORY_REMOVE, removeById)
    }

    private val getAll = Handler { message: Message<JsonObject> ->
        launch(vertx.dispatcher()) {
            try {
                val result: RowSet<Row> = connection.preparedQueryAwait(DeveloperSQLExpressions.SELECT_ALL)

                val developersJSON = JsonArray(result.map { rowMapper(it) })

                message.reply(developersJSON)
            } catch (e: Exception) {
                message.fail(ERROR_ON_RETRIEVING_ALL_DEVELOPERS, e.message)
            }
        }
    }

    private val getById = Handler { message: Message<JsonObject> ->
        launch(vertx.dispatcher()) {
            try {
                val body: JsonObject = message.body()
                val id = body.getString("id")

                val result: RowSet<Row> = connection.preparedQueryAwait(DeveloperSQLExpressions.SELECT_BY_ID, Tuple.of(UUID.fromString(id)))

                val developer = result.map { rowMapper(it) }.first()

                message.reply(developer)
            } catch (e: Exception) {
                message.fail(ERROR_ON_RETRIEVING_ONE_DEVELOPER, e.message)
            }
        }
    }

    private val create = Handler { message: Message<JsonObject> ->
        launch(vertx.dispatcher()) {
            try {
                val developer: JsonObject = message.body()
                developer.put("id", UUID.randomUUID().toString())
                developer.put("created_at", Instant.now())

                connection.preparedQueryAwait(
                    DeveloperSQLExpressions.INSERT,
                    Tuple.of(
                        UUID.fromString(developer.getString("id")),
                        developer.getString("name"),
                        developer.getInteger("age"),
                        developer.getString("expertise"),
                        LocalDateTime.ofInstant(developer.getInstant("created_at"), ZoneOffset.UTC)
                    )
                )

                message.reply(developer)
            } catch (e: Exception) {
                message.fail(ERROR_ON_CREATING_A_DEVELOPER, e.message)
            }
        }
    }

    private val removeById = Handler { message: Message<JsonObject> ->
        launch(vertx.dispatcher()) {
            try {
                val body: JsonObject = message.body()
                val id = body.getString("id")

                connection.preparedQueryAwait(DeveloperSQLExpressions.DELETE_BY_ID, Tuple.of(UUID.fromString(id)))

                message.reply(body)
            } catch (e: Exception) {
                message.fail(ERROR_ON_REMOVING_A_DEVELOPER, e.message)
            }
        }
    }

    private fun rowMapper(row: Row): JsonObject {

        return jsonObjectOf(
            "id" to row.getUUID("id").toString(),
            "name" to row.getString("name"),
            "age" to row.getInteger("age"),
            "expertise" to row.getString("expertise"),
            "created_at" to row.getLocalDateTime("created_at").toString()
        )
    }

    companion object {
        const val DEVELOPER_REPOSITORY_GET_ALL = "DEVELOPER_REPOSITORY_GET_ALL"
        const val DEVELOPER_REPOSITORY_GET_BY_ID = "DEVELOPER_REPOSITORY_GET_BY_ID"
        const val DEVELOPER_REPOSITORY_CREATE = "DEVELOPER_REPOSITORY_CREATE"
        const val DEVELOPER_REPOSITORY_REMOVE = "DEVELOPER_REPOSITORY_REMOVE"
    }
}
