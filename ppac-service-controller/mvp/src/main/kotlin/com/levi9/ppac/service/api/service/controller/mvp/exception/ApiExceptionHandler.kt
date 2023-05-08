package com.levi9.ppac.service.api.service.controller.mvp.exception

import com.levi9.ppac.service.api.logging.logger
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.BAD_REQUEST_MESSAGE
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.CONFLICT_MESSAGE
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.INTERNAL_SERVER_ERROR_MESSAGE
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.INVALID_PARAMETER
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.METHOD_NOT_ALLOWED_MESSAGE
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.MISSING_HEADER
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.NOT_FOUND_MESSAGE
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.TIMEOUT_MESSAGE
import com.levi9.ppac.service.api.service.controller.mvp.constants.ResponseMessages.UNAUTHORIZED_MESSAGE
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
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

@RestControllerAdvice(basePackages = ["com.levi9.ppac.service.api.service.controller"])
@Suppress("TooManyFunctions")
class ApiExceptionHandler {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "400", description = "Bad Request",
                content = [Content(
                    mediaType = "text/plain",
                    schema = Schema(type = "string"),
                    examples = [
                        ExampleObject(
                            name = "INVALID_PARAMETER",
                            value = INVALID_PARAMETER + "accessCode",
                            description = "Expected data type or format do not match"
                        ),
                        ExampleObject(
                            name = "BAD_REQUEST_MESSAGE",
                            value = BAD_REQUEST_MESSAGE,
                            description = """An argument is illegal or invalid, validation failed or
                                  a request body is expected but the received data is not in the expected format"""
                        ),
                        ExampleObject(
                            name = "MISSING_HEADER",
                            value = MISSING_HEADER,
                            description = "A required header is missing from the HTTP request"
                        ),
                        ExampleObject(
                            name = "ConstraintViolationException",
                            value = "Invalid length for requirements field.",
                            description = "Validation failed"
                        )
                    ]
                )]
            )
        ]
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.badRequest().body(BAD_REQUEST_MESSAGE)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.badRequest().body(INVALID_PARAMETER + ex.name)
    }

    @ExceptionHandler(MissingRequestHeaderException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingRequestHeaderException(ex: MissingRequestHeaderException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.badRequest().body(MISSING_HEADER)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.badRequest().body(BAD_REQUEST_MESSAGE)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BAD_REQUEST_MESSAGE)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        val errorMessage = ex.constraintViolations.stream()
            .map { violation -> violation.message }
            .findFirst()
            .orElse(BAD_REQUEST_MESSAGE)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "401", description = "Unauthorized",
                content = [Content(
                    mediaType = "text/plain",
                    schema = Schema(type = "string"),
                    examples = [
                        ExampleObject(
                            value = UNAUTHORIZED_MESSAGE
                        )
                    ]
                )]
            )
        ]
    )
    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UNAUTHORIZED_MESSAGE)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "404", description = "Not Found",
                content = [Content(
                    mediaType = "text/plain",
                    schema = Schema(type = "string"),
                    examples = [
                        ExampleObject(
                            value = NOT_FOUND_MESSAGE
                        )
                    ]
                )]
            )
        ]
    )
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleExceptionEmptyResult(ex: NotFoundException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_MESSAGE)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "409", description = "Conflict",
                content = [Content(
                    mediaType = "text/plain",
                    schema = Schema(type = "string"),
                    examples = [
                        ExampleObject(
                            name = "CONFLICT",
                            value = CONFLICT_MESSAGE,
                            description = "A constraint is violated while performing a database operation"
                        )
                    ]
                )]
            )
        ]
    )
    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleExceptionDataIntegrityViolationException(ex: DataIntegrityViolationException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(CONFLICT_MESSAGE)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(CONFLICT_MESSAGE)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "405", description = "Method Not Allowed",
                content = [Content(
                    mediaType = "text/plain",
                    schema = Schema(type = "string"),
                    examples = [
                        ExampleObject(
                            name = "METHOD_NOT_ALLOWED_MESSAGE",
                            value = METHOD_NOT_ALLOWED_MESSAGE,
                            description = """An HTTP request is made using an unsupported HTTP 
                                method, such as attempting to make a POST request to a resource 
                                that only supports GET requests"""
                        )
                    ]
                )]
            )
        ]
    )
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    fun handlerHttpRequestMethodNotSupportedException(
        ex: HttpRequestMethodNotSupportedException
    ): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(METHOD_NOT_ALLOWED_MESSAGE)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "408", description = "Request Timeout",
                content = [Content(
                    mediaType = "text/plain",
                    schema = Schema(type = "string"),
                    examples = [
                        ExampleObject(
                            name = "TIMEOUT_MESSAGE",
                            value = TIMEOUT_MESSAGE,
                            description = "An operation takes too long to complete within the time limit"
                        )
                    ]
                )]
            )
        ]
    )
    @ExceptionHandler(TimeoutException::class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    fun handleTimeoutException(ex: TimeoutException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(TIMEOUT_MESSAGE)
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "500", description = "Internal Server Error",
                content = [Content(
                    mediaType = "text/plain",
                    schema = Schema(type = "string"),
                    examples = [
                        ExampleObject(
                            name = "INTERNAL_SERVER_ERROR_MESSAGE",
                            value = INTERNAL_SERVER_ERROR_MESSAGE,
                            description = """Due to any unhandled internal error or a database operation that 
                                |fails due to a resource failure, such as a connection failure or a timeout"""
                        )
                    ]
                )]
            )
        ]
    )
    @ExceptionHandler(DataAccessResourceFailureException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleServiceUnavailableException(ex: DataAccessResourceFailureException): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(INTERNAL_SERVER_ERROR_MESSAGE)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalServerError(ex: Exception): ResponseEntity<String> {
        logger.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(INTERNAL_SERVER_ERROR_MESSAGE)
    }

}
