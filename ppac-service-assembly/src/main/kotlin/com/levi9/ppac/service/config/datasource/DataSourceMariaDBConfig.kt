package com.levi9.ppac.service.config.datasource

import com.levi9.ppac.service.api.database.DataSourceFactory
import com.levi9.ppac.service.api.database.UserAccess
import org.hibernate.jpa.HibernatePersistenceProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
        basePackages = ["com.levi9.ppac.service.api.repository"],
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
)
class DataSourceMariaDBConfig(
        private val dataSourceFactory: DataSourceFactory,
        private val env: Environment
) {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    @Primary
    fun dataSource(): DataSource {
        return dataSourceFactory.createDataSource(UserAccess.READ_WRITE)
    }

    @Bean
    @Primary
    fun entityManagerFactory(dataSourceBean: DataSource): LocalContainerEntityManagerFactoryBean {
        val properties: HashMap<String, Any?> = HashMap()
        properties["hibernate.hbm2ddl.auto"] = env.getProperty("sql.hibernate.ddl-auto")
        properties["hibernate.generate_statistics"] = env.getProperty("sql.properties.hibernate.generate_statistics")
        properties["org.hibernate.envers.audit_table_suffix"] =
                env.getProperty("sql.properties.org.hibernate.envers.audit_table_suffix")
        properties["hibernate.physical_naming_strategy"] =
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"

        return object : LocalContainerEntityManagerFactoryBean() {
            init {
                dataSource = dataSourceBean
                setPersistenceProviderClass(HibernatePersistenceProvider::class.java)
                jpaVendorAdapter = HibernateJpaVendorAdapter()
                setPackagesToScan(
                        "com.levi9.ppac.service.api.domain"
                )
                setJpaPropertyMap(properties)
            }
        }
    }

    @Bean
    @Primary
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}
