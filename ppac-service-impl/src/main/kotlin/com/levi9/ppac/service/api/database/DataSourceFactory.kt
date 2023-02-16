package com.levi9.ppac.service.api.database

import com.zaxxer.hikari.HikariConfig
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Value

abstract class DataSourceFactory(
    private val sqlConfigurations: SqlConfigurations
) {
    @Value("\${spring.datasource.hikari.connectionTimeout}")
    private val connectionTimeout: Long? = null

    @Value("\${spring.datasource.hikari.maximumPoolSize}")
    private val maximumPoolSize: Int? = null

    abstract fun createDataSource(userAccess: UserAccess): DataSource

    protected fun getBaseConfigurations(userAccess: UserAccess): HikariConfig {
        val config = HikariConfig()
        config.username = when (userAccess) {
            UserAccess.DB_ADMIN -> sqlConfigurations.dbAdminUsername
            UserAccess.APP_ADMIN -> sqlConfigurations.appAdminUsername
            UserAccess.READ_WRITE -> sqlConfigurations.readWriteUsername
            UserAccess.READ_ONLY -> sqlConfigurations.readOnlyUsername
        }
        config.password = when (userAccess) {
            UserAccess.DB_ADMIN -> sqlConfigurations.dbAdminPassword
            UserAccess.APP_ADMIN -> sqlConfigurations.appAdminPassword
            UserAccess.READ_WRITE -> sqlConfigurations.readWritePassword
            UserAccess.READ_ONLY -> sqlConfigurations.readOnlyPassword
        }
        config.schema = sqlConfigurations.schema
        if (connectionTimeout != null) {
            config.connectionTimeout = connectionTimeout
        }
        if (maximumPoolSize != null) {
            config.maximumPoolSize = maximumPoolSize
        }
        return config
    }
}
