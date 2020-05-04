package com.rafaelcam.vertx.infra

import io.vertx.config.ConfigRetriever
import io.vertx.core.Vertx
import io.vertx.kotlin.config.configRetrieverOptionsOf
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.core.json.jsonObjectOf

object ConfigRetriever {

    fun getConfig(vertx: Vertx): ConfigRetriever =
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
}
