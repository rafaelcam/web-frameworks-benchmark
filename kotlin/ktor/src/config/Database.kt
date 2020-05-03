package com.rafaelcam.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

fun dbConnect() {
    Database.connect(getConnection())
}

private fun getConnection(): HikariDataSource {
    val config = HikariConfig()

    config.driverClassName = "org.postgresql.Driver"
    config.jdbcUrl = "jdbc:postgresql://localhost:54320/web-frameworks-benchmark"
    config.username = "postgres"
    config.password = "postgres"
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

    config.addDataSourceProperty("cachePrepStmts", "true")
    config.addDataSourceProperty("prepStmtCacheSize", "250")
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    config.addDataSourceProperty("minimumIdle", "10")
    config.addDataSourceProperty("maximumPoolSize", "20")
    config.addDataSourceProperty("idleTimeout", "1800000")
    config.addDataSourceProperty("leakDetectionThreshold", "8000")
    config.validate()

    return HikariDataSource(config)
}