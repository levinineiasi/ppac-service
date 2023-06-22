package com.levi9.ppac.service.api.database

import com.zaxxer.hikari.HikariDataSource
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
internal class URLDataSourceFactory(
    val sqlConfigurations: SqlConfigurations
) : DataSourceFactory(sqlConfigurations) {

    override fun createDataSource(userAccess: UserAccess): DataSource {
        val baseConfig = getBaseConfigurations(userAccess)
        baseConfig.jdbcUrl = "jdbc:mariadb://${sqlConfigurations.instance}/${sqlConfigurations.database}?useUnicode=true&amp;character_set_server=utf8mb4"
        return HikariDataSource(baseConfig)
    }
}
