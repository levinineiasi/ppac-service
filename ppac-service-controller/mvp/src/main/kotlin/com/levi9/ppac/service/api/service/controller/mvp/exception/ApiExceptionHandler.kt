package com.levi9.ppac.service.api.service.controller.mvp.exception

import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.concurrent.TimeoutException
import javax.naming.AuthenticationException
import javax.validation.ConstraintViolationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException

@RestControllerAdvice(basePackages = ["com.levi9.ppac.service.api.service.controller"])
class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.badRequest().body(ResponseMessages.BAD_REQUEST_MESSAGE)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<String> {
        logger.error(ex.message, ex)

        return ResponseEntity.badRequest().body(ResponseMessages.INVALID_PARAMETER + ex.name)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingRequestHeaderException(ex: MissingRequestHeaderException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.badRequest().body(ResponseMessages.MISSING_HEADER)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.badRequest().body(ResponseMessages.BAD_REQUEST_MESSAGE)
    }

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseMessages.UNAUTHORIZED_MESSAGE)
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleExceptionEmptyResult(ex: NotFoundException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessages.NOT_FOUND_MESSAGE)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        val errorMessage = ex.constraintViolations.stream()
            .map { violation -> violation.message }
            .findFirst()
            .orElse(ResponseMessages.BAD_REQUEST_MESSAGE)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }


    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleExceptionDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ResponseMessages.CONFLICT_MESSAGE)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handlerHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ResponseMessages.METHOD_NOT_ALLOWED_MESSAGE)
    }

    @ExceptionHandler(TimeoutException::class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    fun handleTimeoutException(ex: TimeoutException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(ResponseMessages.TIMEOUT_MESSAGE)
    }

    @ExceptionHandler(DataAccessResourceFailureException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleServiceUnavailableException(ex: DataAccessResourceFailureException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseMessages.INTERNAL_SERVER_ERROR_MESSAGE)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalServerError(ex: Exception): ResponseEntity<Any> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseMessages.INTERNAL_SERVER_ERROR_MESSAGE)
    }
}