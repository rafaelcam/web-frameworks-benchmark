package com.rafaelcam.vertx.infra

import com.rafaelcam.vertx.webservice.DeveloperResourceHandler
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.ResponseContentTypeHandler
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle

class ServerVerticle : CoroutineVerticle() {

    override suspend fun start() {
        val developerRouter: Router = Router.router(vertx).also { router: Router ->
            setupRouter(router)

            router.get("/api/v1/developers").handler(DeveloperResourceHandler.getAll)
            router.get("/api/v1/developers/:id").handler(DeveloperResourceHandler.getById)
            router.post("/api/v1/developers").handler(DeveloperResourceHandler.create)
            router.delete("/api/v1/developers/:id").handler(DeveloperResourceHandler.remove)
        }

        val httpServer: HttpServer = vertx.createHttpServer()
                .requestHandler(developerRouter::handle)
                .listenAwait(config.getJsonObject("server").getInteger("port"))

        println("HTTP Server is running on port ${httpServer.actualPort()}")
    }

    private fun setupRouter(router: Router): Route =
            router.route()
                    .handler(BodyHandler.create())
                    .handler(ResponseContentTypeHandler.create())
                    .produces("application/json")
}