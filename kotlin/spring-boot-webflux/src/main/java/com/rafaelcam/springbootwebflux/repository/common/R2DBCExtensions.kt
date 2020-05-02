package com.rafaelcam.springbootwebflux.repository.common

import io.r2dbc.spi.Row
import org.springframework.data.r2dbc.core.DatabaseClient.GenericExecuteSpec

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T> Row.get(identifier: String): T = this.get(identifier, T::class.java)!!

inline fun <reified T> Row.getOrNull(identifier: String): T? = this.get(identifier, T::class.java)

inline fun <reified T> GenericExecuteSpec.bindOrNull(name: String, value: T?) =
        value?.let { this.bind(name, it as Any) } ?: this.bindNull(name, T::class.java)