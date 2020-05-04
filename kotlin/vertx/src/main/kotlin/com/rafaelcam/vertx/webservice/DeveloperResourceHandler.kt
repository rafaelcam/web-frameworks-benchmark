package com.rafaelcam.vertx.webservice

import com.rafaelcam.vertx.service.DeveloperService
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.ReplyException
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.eventbus.requestAwait
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DeveloperResourceHandler {

    val getAll = Handler { rc: RoutingContext ->
        GlobalScope.launch(rc.vertx().dispatcher()) {
            try {
                val developers: Message<JsonArray> = rc.vertx().eventBus().requestAwait(
                    DeveloperService.DEVELOPER_SERVICE_GET_ALL,
                    jsonObjectOf()
                )

                rc.response()
                    .setStatusCode(200)
                    .end(developers.body().toBuffer())
            } catch (e: Exception) {
                handleError(rc, e)
            }
        }
    }

    val getById = Handler { rc: RoutingContext ->
        GlobalScope.launch(rc.vertx().dispatcher()) {
            try {
                val id = rc.pathParam("id")
                val jsonObject = jsonObjectOf(
                    "id" to id
                )

                val developer: Message<JsonObject> = rc.vertx().eventBus().requestAwait(
                    DeveloperService.DEVELOPER_SERVICE_GET_BY_ID, jsonObject
                )

                rc.response()
                    .setStatusCode(200)
                    .end(developer.body().toBuffer())
            } catch (e: Exception) {
                handleError(rc, e)
            }
        }
    }

    val create = Handler { rc: RoutingContext ->
        GlobalScope.launch(rc.vertx().dispatcher()) {
            try {
                val developer: Message<JsonObject> = rc.vertx().eventBus().requestAwait(
                    DeveloperService.DEVELOPER_SERVICE_CREATE, rc.bodyAsJson
                )

                rc.response()
                    .setStatusCode(201)
                    .end(developer.body().toBuffer())
            } catch (e: Exception) {
                handleError(rc, e)
            }
        }
    }

    val remove = Handler { rc: RoutingContext ->
        GlobalScope.launch(rc.vertx().dispatcher()) {
            try {
                val id = rc.pathParam("id")
                val jsonObject = jsonObjectOf(
                    "id" to id
                )

                val developer: Message<JsonObject> = rc.vertx().eventBus().requestAwait(
                    DeveloperService.DEVELOPER_SERVICE_REMOVE, jsonObject
                )

                rc.response()
                    .setStatusCode(200)
                    .end(developer.body().toBuffer())
            } catch (e: Exception) {
                handleError(rc, e)
            }
        }
    }

    private fun handleError(rc: RoutingContext, e: Exception) {
        rc.response()
            .setStatusCode(if (e is ReplyException) 400 else 500)
            .end(
                jsonObjectOf(
                    "error_code" to if (e is ReplyException) e.failureCode() else Int.MIN_VALUE,
                    "message" to e.message
                ).toBuffer()
            )
    }
}
