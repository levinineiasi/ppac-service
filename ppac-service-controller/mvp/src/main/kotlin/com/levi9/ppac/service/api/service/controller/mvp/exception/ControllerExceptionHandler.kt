package com.levi9.ppac.service.api.service.controller.mvp.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import java.sql.SQLException

@ControllerAdvice(basePackages = ["com.levi9.ppac.service.api.service.controller"])
class ControllerExceptionHandler {
    @ExceptionHandler(SQLException::class)
    fun handleException(ex: SQLException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid Request")
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleExceptionUnauthorized(ex: ResponseStatusException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You are not authorized to do this")
    }
}
