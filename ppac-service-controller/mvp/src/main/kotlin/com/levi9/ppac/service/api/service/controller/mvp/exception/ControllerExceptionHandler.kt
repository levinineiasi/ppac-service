package com.levi9.ppac.service.api.service.controller.mvp.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import org.webjars.NotFoundException
import java.sql.SQLException
import javax.validation.ConstraintViolationException


//@ControllerAdvice(basePackages = ["com.levi9.ppac.service.api.service.controller"])
class ControllerExceptionHandler {
    @ExceptionHandler(SQLException::class)
    fun handleException(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid Request.")
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleExceptionUnauthorized(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You are not authorized to do this.")
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleExceptionEmptyResult(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The requested resource was not found.")
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException
    ): ResponseEntity<Any?>? {
        val error = ex.constraintViolations.stream()
            .map { violation -> violation.message }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<Any?>? {
        val error = ex.fieldError?.defaultMessage
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }
}
