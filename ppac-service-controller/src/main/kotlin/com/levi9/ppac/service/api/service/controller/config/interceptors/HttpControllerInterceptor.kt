package com.levi9.ppac.service.api.service.controller.config.interceptors

import com.levi9.ppac.service.api.exception.MarkerDescriptor
import com.levi9.ppac.service.api.logging.logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.MarkerFactory

class HttpControllerInterceptor : AbstractInterceptor() {
    override fun logStartProcessing(request: HttpServletRequest, response: HttpServletResponse, handler: Any) {
        logger.info(
            MarkerFactory.getMarker(MarkerDescriptor.REQUEST_START_PROCESSING.name),
            "Entering operation: ${request.method} ${request.requestURI} " +
                "\nHeaders ${request.getHeadersAsString()} \nParameters ${request.getParametersAsString()}"
        )
    }

    override fun logFinishProcessing(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        logger.info(
            MarkerFactory.getMarker(MarkerDescriptor.REQUEST_COMPLETION_PROCESSING.name),
            "Completing operation: ${request.method} ${request.requestURI} with status " +
                "${response.status}"
        )
    }
}
