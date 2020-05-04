package com.rafaelcam.vertx.service

import com.rafaelcam.vertx.errorcode.ERROR_GENERAL_EXCEPTION
import com.rafaelcam.vertx.repository.DeveloperRepository
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.ReplyException
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.eventbus.requestAwait
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DeveloperService : CoroutineVerticle() {

    override suspend fun start() {
        vertx.eventBus().consumer(DEVELOPER_SERVICE_GET_ALL, getAll)
        vertx.eventBus().consumer(DEVELOPER_SERVICE_GET_BY_ID, getById)
        vertx.eventBus().consumer(DEVELOPER_SERVICE_CREATE, create)
        vertx.eventBus().consumer(DEVELOPER_SERVICE_REMOVE, removeById)
    }

    private val getAll = Handler { message: Message<JsonObject> ->
        GlobalScope.launch(vertx.dispatcher()) {
            try {
                val developers: Message<JsonArray> = vertx.eventBus().requestAwait(
                    DeveloperRepository.DEVELOPER_REPOSITORY_GET_ALL,
                    jsonObjectOf()
                )

                message.reply(developers.body())
            } catch (re: ReplyException) {
                message.fail(re.failureCode(), re.message)
            } catch (e: Exception) {
                message.fail(ERROR_GENERAL_EXCEPTION, e.message)
            }
        }
    }

    private val getById = Handler { message: Message<JsonObject> ->
        GlobalScope.launch(vertx.dispatcher()) {
            try {
                val jsonObject: JsonObject = message.body()

                val developer: Message<JsonObject> = vertx.eventBus().requestAwait(
                    DeveloperRepository.DEVELOPER_REPOSITORY_GET_BY_ID, jsonObject
                )

                message.reply(developer.body())
            } catch (re: ReplyException) {
                message.fail(re.failureCode(), re.message)
            } catch (e: Exception) {
                message.fail(ERROR_GENERAL_EXCEPTION, e.message)
            }
        }
    }

    private val create = Handler { message: Message<JsonObject> ->
        GlobalScope.launch(vertx.dispatcher()) {
            try {
                val developer: JsonObject = message.body()

                val newPerson: Message<JsonObject> = vertx.eventBus().requestAwait(
                    DeveloperRepository.DEVELOPER_REPOSITORY_CREATE,
                    developer
                )

                message.reply(newPerson.body())
            } catch (re: ReplyException) {
                message.fail(re.failureCode(), re.message)
            } catch (e: Exception) {
                message.fail(ERROR_GENERAL_EXCEPTION, e.message)
            }
        }
    }

    private val removeById = Handler { message: Message<JsonObject> ->
        GlobalScope.launch(vertx.dispatcher()) {
            try {
                val jsonObject: JsonObject = message.body()

                val developer: Message<JsonObject> = vertx.eventBus().requestAwait(
                    DeveloperRepository.DEVELOPER_REPOSITORY_REMOVE, jsonObject
                )

                message.reply(developer.body())
            } catch (re: ReplyException) {
                message.fail(re.failureCode(), re.message)
            } catch (e: Exception) {
                message.fail(ERROR_GENERAL_EXCEPTION, e.message)
            }
        }
    }

    companion object {
        const val DEVELOPER_SERVICE_GET_ALL = "DEVELOPER_SERVICE_GET_ALL"
        const val DEVELOPER_SERVICE_GET_BY_ID = "DEVELOPER_SERVICE_GET_BY_ID"
        const val DEVELOPER_SERVICE_CREATE = "DEVELOPER_SERVICE_CREATE"
        const val DEVELOPER_SERVICE_REMOVE = "DEVELOPER_SERVICE_REMOVE"
    }
}
