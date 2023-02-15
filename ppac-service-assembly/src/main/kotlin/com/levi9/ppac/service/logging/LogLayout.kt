package com.levi9.ppac.service.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import ch.qos.logback.core.LayoutBase
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.levi9.ppac.service.api.exception.MarkerDescriptor
import com.levi9.ppac.service.config.ApiConfig

class LogLayout : LayoutBase<ILoggingEvent>() {
    private val mapper: ObjectMapper = ApiConfig().getObjectMapper()

    init {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    override fun doLayout(event: ILoggingEvent): String {
        val entry = try {
            val message = StringBuffer()
            message.append("${event.formattedMessage}\n")
            message.append(
                event.throwableProxy?.let {
                    " ${
                        ThrowableProxyUtil.asString(
                            it
                        )
                    }"
                } ?: "")
            val mdc = event.mdcPropertyMap
            LogEntry(
                severity = event.level.toString(),
                message = message.toString(),
                labels = Labels(
                    marker = event.marker?.name,
                    correlationId = mdc["X-Correlation-ID"],
                    executionTime = mdc["execution-time"],
                    waitTime = mdc["wait-time"],
                    buildNum = System.getenv(
                        "INFO_APP_BUILD"
                    ),
                    processType = mdc["process-type"],
                    loggerName = event.loggerName,
                    threadName = event.threadName,
                    consumerId = mdc["X-Consumer-Custom-ID"],
                    consumerUsername = mdc["X-Consumer-Username"]
                )
            )
        } catch (exception: Exception){
            LogEntry(
                severity = Level.ERROR.toString(),
                message = "Log entry was lost! Caused by ${exception.message}",
                labels = Labels(
                    marker = MarkerDescriptor.LOG_MESSAGE_LOST.name,
                    buildNum = System.getenv(
                        "INFO_APP_BUILD"
                    ),
                    loggerName = event.loggerName,
                    threadName = event.threadName
                )
            )
        }
        return mapper.writeValueAsString(entry) + "\n"
    }
}

private data class LogEntry(
    val severity: String,
    val message: String,
    @get:JsonProperty(value = "logging.googleapis.com/labels")
    val labels: Labels
)

private data class Labels(
    @get:JsonProperty(value = "logback-marker")
    val marker: String? = null,
    @get:JsonProperty(value = "thread-name")
    val threadName: String,
    @get:JsonProperty(value = "logger-name")
    val loggerName: String,
    @get:JsonProperty(value = "correlation-id")
    val correlationId: String? = null,
    @get:JsonProperty(value = "execution-time")
    val executionTime: String? = null,
    @get:JsonProperty(value = "wait-time")
    val waitTime: String? = null,
    @get:JsonProperty(value = "consumer-id")
    val consumerId: String? = null,
    @get:JsonProperty(value = "consumer-username")
    val consumerUsername: String? = null,
    @get:JsonProperty(value = "info-app-build")
    val buildNum: String,
    @get:JsonProperty(value = "process-type")
    val processType: String? = null,
)
