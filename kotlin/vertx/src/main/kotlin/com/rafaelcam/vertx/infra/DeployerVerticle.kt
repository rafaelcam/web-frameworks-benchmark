package com.rafaelcam.vertx.infra

import com.rafaelcam.vertx.repository.DeveloperRepository
import com.rafaelcam.vertx.service.DeveloperService
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.coroutines.CoroutineVerticle

class DeployerVerticle : CoroutineVerticle() {

    override suspend fun start() {
        deployPeopleVerticles()
    }

    private suspend fun deployPeopleVerticles() {
        val developerServiceVerticleId: String = vertx.deployVerticleAwait(
            DeveloperService::class.java.name, deploymentOptionsOf(
                config = config
            )
        )
        println("- ${DeveloperService::class.java.name} verticle was deployed [$developerServiceVerticleId]")

        val developerRepositoryVerticleId: String = vertx.deployVerticleAwait(
            DeveloperRepository::class.java.name, deploymentOptionsOf(
                config = config
            )
        )
        println("- ${DeveloperRepository::class.java.name} verticle was deployed [$developerRepositoryVerticleId]")
    }
}
