package com.levi9.ppac.service.api.exception

import org.springframework.http.HttpStatus

@Suppress("MagicNumber", "MaxLineLength")
enum class MarkerDescriptor(
    val httpStatus: HttpStatus,
    val reason: String
) {
    HTTP_GATEWAY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error when calling external service"),
    LOG_MESSAGE_LOST(HttpStatus.INTERNAL_SERVER_ERROR, "Log message was lost"),
}
