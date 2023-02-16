package com.levi9.ppac.service.api.database

import com.zaxxer.hikari.HikariDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
internal class LocalDataSourceFactory(
    val sqlConfigurations: SqlConfigurations
) : DataSourceFactory(sqlConfigurations) {

    override fun createDataSource(userAccess: UserAccess): DataSource {
        val baseConfig = getBaseConfigurations(userAccess)
        baseConfig.jdbcUrl = "jdbc:postgresql://${sqlConfigurations.instance}/${sqlConfigurations.database}"
        return HikariDataSource(baseConfig)
    }
}
