package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.exception.EntityNotFoundException
import co.zip.candidate.userapi.exception.UserValidationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import java.util.function.Consumer

@ControllerAdvice
class ClientExceptionHandler {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseBody
    fun handleValidationExceptions(e: HttpMessageNotReadableException): Map<String, String> {
        log.error("payload is not readable", e)
        return mapOf("message" to "payload is not readable")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun handleValidationExceptions(e: MethodArgumentNotValidException): Map<String, String> {
        log.error("payload validation failed", e)
        val errors: MutableMap<String, String> = mutableMapOf()
        e.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = (error as FieldError).field
            error.getDefaultMessage()?.let {
                errors[fieldName] = it
            }
        })
        return errors
    }

    @ExceptionHandler(ResponseStatusException::class)
    @ResponseBody
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<Map<String, String>> {
        log.error("error while processing payload", e)
        return ResponseEntity.status(e.status)
            .body(mapOf("message" to (e.reason ?: "internal error")))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserValidationException::class)
    @ResponseBody
    fun handleZipValidationException(e: UserValidationException): Map<String, String> {
        log.error("Validation failed", e)
        return mapOf("message" to e.message)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseBody
    fun handleEntityNotFoundException(e: EntityNotFoundException): Map<String, String> {
        log.error("Entity not found", e)
        return mapOf("message" to e.message)
    }

}