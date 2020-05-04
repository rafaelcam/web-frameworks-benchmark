package com.rafaelcam.vertx.infra

import io.vertx.core.Handler
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun Route.coroutineHandler(fn: Handler<RoutingContext>): Route {
    return handler { rc: RoutingContext ->
        GlobalScope.launch(rc.vertx().dispatcher()) {
            try {
                fn.handle(rc)
            } catch (e: Exception) {
                rc.fail(e)
            }
        }
    }
}
