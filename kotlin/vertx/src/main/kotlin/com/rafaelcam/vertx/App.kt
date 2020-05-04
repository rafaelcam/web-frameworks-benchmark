package com.rafaelcam.vertx

import com.rafaelcam.vertx.infra.DeployerVerticle
import com.rafaelcam.vertx.infra.ServerVerticle
import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.kotlin.config.configRetrieverOptionsOf
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.config.getConfigAwait
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.core.json.jsonObjectOf

suspend fun main() {
    try {
        val vertx: Vertx = Vertx.vertx()
        val configRetriever: ConfigRetriever = getConfig(vertx)
        // Deploying service/repository verticles
        println("<<< Deploying verticles...")
        val deployerVerticleId: String = vertx.deployVerticleAwait(
            DeployerVerticle::class.java.name, deploymentOptionsOf(
                config = configRetriever.getConfigAwait()
            )
        )
        println("- ${DeployerVerticle::class.java.name} was deployed [$deployerVerticleId]")
        // Deploying server verticle
        val serverVerticleId: String = vertx.deployVerticleAwait(
            ServerVerticle::class.java.name, deploymentOptionsOf(
                config = configRetriever.getConfigAwait()
            )
        )
        println("- ${ServerVerticle::class.java.name} was deployed [$serverVerticleId]")
        println(">>> OK! Application is running...")
    } catch (e: Exception) {
        println("*** There was an error while starting application: ${e.message}")
    }
}

private fun getConfig(vertx: Vertx): ConfigRetriever =
    ConfigRetriever.create(
        vertx, configRetrieverOptionsOf(
            stores = listOf(
                configStoreOptionsOf(
                    type = "file",
                    config = jsonObjectOf(
                        "path" to "app.json"
                    )
                )
            )
        )
    )
