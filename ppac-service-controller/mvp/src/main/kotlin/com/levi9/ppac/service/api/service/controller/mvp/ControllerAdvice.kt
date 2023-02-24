package com.levi9.ppac.service.api.service.controller.mvp

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ControllerAdvice {

//    @ExceptionHandler(value = [DataIntegrityViolationException::class])
//    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<Map<String, Any>> {
//        val errorMessage = "A database constraint violation occurred."
//        val details = ex.rootCause?.message
//        val errorResponse = mapOf(
//            "error" to "Bad Request",
//            "message" to errorMessage,
//            "details" to details
//        )
//        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
//    }
}