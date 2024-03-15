package com.example.delivery

import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName.parse

fun postgres(version: String, options: JdbcDatabaseContainer<Nothing>.() -> Unit) =
    PostgreSQLContainer<Nothing>(parse("postgres:$version")).apply(options)
