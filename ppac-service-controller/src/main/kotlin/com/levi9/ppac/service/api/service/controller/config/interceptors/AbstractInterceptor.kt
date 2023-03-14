package com.levi9.ppac.service.api.service.controller.config.interceptors

import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

abstract class AbstractInterceptor : HandlerInterceptor {
    private val startTime = ThreadLocal<Long>()

    abstract fun logStartProcessing(request: HttpServletRequest, response: HttpServletResponse, handler: Any)
    abstract fun logFinishProcessing(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    )

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        startTime.initWithCurrentTime()

        MDC.put("X-Correlation-ID", request.getCorrelationId())

        request.getHeader("X-Consumer-Custom-ID")?.let { consumerId ->
            MDC.put("X-Consumer-Custom-ID", consumerId)
        }

        request.getHeader("X-Consumer-Username")?.let { consumerUsername ->
            MDC.put("X-Consumer-Username", consumerUsername)
        }
        handler.takeIf { it is HandlerMethod }?.let {
            val methodHandler = it as HandlerMethod
            MDC.put("process-type", methodHandler.method.name)
        } ?: MDC.put("process-type", request.requestURI)

        logStartProcessing(request, response, handler)
        return true
    }


    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        MDC.put("execution-time", startTime.calculateExecutionTime().toString())
        logFinishProcessing(request, response, handler, ex)
        MDC.clear()
    }

    protected fun HttpServletRequest.getHeadersAsString(): String {
        val headers = StringBuilder("Request headers:")
        headerNames.iterator().forEachRemaining { headerName ->
            headers.append("\n${headerName}: ${getHeader(headerName)}")
        }
        return headers.toString()
    }

    protected fun HttpServletRequest.getParametersAsString(): String {
        val parameters = StringBuilder("Request parameters:")
        parameterNames.iterator().forEachRemaining { parameterName ->
            parameters.append("\n${parameterName}=${getParameter(parameterName)}")
        }
        return parameters.toString()
    }

    private fun ThreadLocal<Long>.initWithCurrentTime() = set(System.currentTimeMillis())

    private fun ThreadLocal<Long>.calculateExecutionTime() = System.currentTimeMillis() - get()

    private fun HttpServletRequest.getCorrelationId() =
        getHeader("X-Correlation-ID") ?: getHeader("Request-ID") ?: UUID.randomUUID().toString()
}
