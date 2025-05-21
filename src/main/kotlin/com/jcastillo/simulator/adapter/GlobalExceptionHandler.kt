package com.jcastillo.simulator.adapter

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.jcastillo.simulator.port.model.Error
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.resource.NoResourceFoundException
private const val INVALID_FILE_FORMAT = "Invalid file format"
private const val INVALID_JSON_FORMAT = "Invalid JSON format"
private const val FILE_IS_EMPTY = "File is empty"
private const val FILE_IS_MISSING = "File is missing"
private const val UNKNOWN_ERROR = "Unknown Error"
private const val NEGATIVE_DAYS = "Invalid number of days. Must be at least 1 and less than 9125"
private const val NOT_FOUND = "Not found"

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(JsonParseException::class, JsonMappingException::class)
    fun handleJsonFormatException(ex: Exception): ResponseEntity<Error> {
        logger.warn("JSON parsing error: ${ex.message}")
        val error = Error(
            code = Error.Code.vALIDATIONERROR,
            message = INVALID_JSON_FORMAT
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error)
    }

    @ExceptionHandler(EmptyFileException::class)
    fun handleEmptyFileException(ex: EmptyFileException): ResponseEntity<Error> {
        logger.warn("Empty file uploaded: ${ex.message}")
        val error = Error(
            code = Error.Code.vALIDATIONERROR,
            message = FILE_IS_EMPTY
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error)
    }

    @ExceptionHandler(MissingServletRequestPartException::class)
    fun handleMissingFileException(ex: MissingServletRequestPartException): ResponseEntity<Error> {
        logger.warn("Missing file exception: ${ex.message}")
        val error = Error(
            code = Error.Code.vALIDATIONERROR,
            message = FILE_IS_MISSING
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error)
    }

    @ExceptionHandler(InvalidFileFormatException::class)
    fun handleInvalidFileException(ex: InvalidFileFormatException): ResponseEntity<Error> {
        logger.warn("Invalid file exception: ${ex.message}")
        val error = Error(
            code = Error.Code.vALIDATIONERROR,
            message = INVALID_FILE_FORMAT
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error)
    }

    @ExceptionHandler(NegativeNumberOfDaysException::class)
    fun handleNegativeNumberOfDaysException(ex: NegativeNumberOfDaysException): ResponseEntity<Error> {
        logger.warn("Negative days: ${ex.message}")
        val error = Error(
            code = Error.Code.vALIDATIONERROR,
            message = NEGATIVE_DAYS
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Error> {
        logger.warn("Validation failed: ${ex.message}")
        val error = Error(
            code = Error.Code.vALIDATIONERROR,
            message = ex.constraintViolations.joinToString(", ") { it.message }
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(ex: NoResourceFoundException): ResponseEntity<Error> {
        logger.error(NOT_FOUND, ex)
        val error = Error(
            code = Error.Code.rESOURCENOTFOUND,
            message = NOT_FOUND
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error)
    }

    @ExceptionHandler(Throwable::class)
    fun handleGenricException(ex: Throwable): ResponseEntity<Error> {
        logger.error("Unhandled exception occurred", ex)
        val error = Error(
            code = Error.Code.iNTERNALERROR,
            message = UNKNOWN_ERROR
        )
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(error)
    }
}