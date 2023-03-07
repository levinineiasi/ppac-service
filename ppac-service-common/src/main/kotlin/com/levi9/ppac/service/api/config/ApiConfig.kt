@file:Suppress("detekt.MagicNumber")

package com.levi9.ppac.service.api.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import java.io.IOException
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Configuration
@EnableAsync
class ApiConfig {

    private val cacheInitSize = 1000
    private val cacheMaxSize = 10000L

    @Bean
    fun getObjectMapper(): ObjectMapper {
        val simpleModule = SimpleModule()
        simpleModule.addSerializer(OffsetDateTime::class.java, object : JsonSerializer<OffsetDateTime?>() {
            @Throws(IOException::class, JsonProcessingException::class)
            override fun serialize(
                offsetDateTime: OffsetDateTime?,
                jsonGenerator: JsonGenerator,
                serializerProvider: SerializerProvider?
            ) {
                jsonGenerator.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime))
            }
        })
        return jacksonObjectMapper()
            .findAndRegisterModules()
            .registerKotlinModule().registerModule(JavaTimeModule())
            .registerModule(simpleModule)
            .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    @Bean
    fun caffeineConfig(): Caffeine<Any, Any> {
        return Caffeine.newBuilder()
            .initialCapacity(cacheInitSize)
            .maximumSize(cacheMaxSize)
            .expireAfterAccess(1, TimeUnit.HOURS)
    }

    @Bean
    fun cacheManager(caffeine: Caffeine<Any, Any>): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(caffeine)
        return caffeineCacheManager
    }
}
