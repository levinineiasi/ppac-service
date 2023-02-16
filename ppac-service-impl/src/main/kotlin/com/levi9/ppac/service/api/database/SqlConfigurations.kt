package com.levi9.ppac.service.api.database

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "sql")
data class SqlConfigurations(
        var instance: String = "",
        var database: String = "",
        var schema: String = "",
        var dbAdminUsername: String = "",
        var dbAdminPassword: String = "",
        var appAdminUsername: String = "",
        var appAdminPassword: String = "",
        var readWriteUsername: String = "",
        var readWritePassword: String = "",
        var readOnlyUsername: String = "",
        var readOnlyPassword: String = "",
        var businessReadOnlyUsername: String = "",
        var businessReadOnlyPassword: String = "",
)
