package com.levi9.ppac.service

import com.levi9.ppac.service.logging.PropertyLogger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.event.EventListener

@SpringBootApplication
@EnableCaching
@EnableFeignClients
class ApiApplication {
    @EventListener(value = [ApplicationReadyEvent::class])
    fun printPropertiesOnReady(event: ApplicationReadyEvent) {
        PropertyLogger.printPropertiesOnReady(event)
    }
}

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    PropertyLogger.printPropertiesOnStartup()
    runApplication<ApiApplication>(*args)
}
