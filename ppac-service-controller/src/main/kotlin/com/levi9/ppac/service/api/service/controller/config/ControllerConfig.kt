package com.levi9.ppac.service.api.service.controller.config

import com.levi9.ppac.service.api.service.controller.config.interceptors.HttpControllerInterceptor
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.MappedInterceptor


@Configuration
internal class ControllerConfig {

    companion object {
        private const val responseTime = 1000
    }

    @Value("\${server.allowed-origins}")
    private val allowedOrigins: String? = null

    @Bean
    fun openApiConfig(): OpenAPI? {
        return OpenAPI()
            .components(
                Components()
            )
            .info(
                Info().title("PPAC API").version("1.0")
                    .description("This documentation describes PPAC API.")
//                    TODO: put correct license here, not Apache 2.0; tbd after mvp
//                    .license(License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0"))
                    .contact(Contact().email("c.baleanu@levi9.com").name("Codrin Baleanu"))
            )
            .extensions(
                mapOf(
                    Pair("x-apiname", "ppac"),
                    Pair("x-responsetime", responseTime),
                    Pair("basePath", "/api/v1/") // stoplight swagger_2 compatibility
                )
            )
    }

    @Bean
    fun openApiGroupV1(): GroupedOpenApi? {
        val paths = arrayOf("/api/v1/**")
        val packages = arrayOf("com.levi9.ppac.service.api.service.controller.mvp")
        return GroupedOpenApi.builder().group("v1").pathsToMatch(*paths).packagesToScan(*packages).build()
    }

    @Bean
    fun corsFilter(): CorsFilter? {
        val source = UrlBasedCorsConfigurationSource()

        // Allow anyone and anything access. Probably ok for Swagger spec
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin(allowedOrigins)
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/api/v1/", config)
        return CorsFilter(source)
    }

    @Component
    internal class ControllerInterceptorConfig : WebMvcConfigurer {
        override fun addInterceptors(registry: InterceptorRegistry) {
            registry.addInterceptor(
                MappedInterceptor(
                    arrayOf("/api/v1/**"),
                    arrayOf(),
                    HttpControllerInterceptor()
                )
            )
        }
    }
}
