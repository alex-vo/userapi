package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.exception.EmailAlreadyTakenException
import co.zip.candidate.userapi.exception.EntityNotFoundException
import co.zip.candidate.userapi.exception.SalaryExpensesRatioException
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SalaryExpensesRatioException::class)
    @ResponseBody
    fun handleSalaryExpensesRatioException(e: SalaryExpensesRatioException): ResponseEntity<Map<String, String>> {
        log.error(e.message, e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("message" to e.message))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseBody
    fun handleEntityNotFoundException(e: EntityNotFoundException): ResponseEntity<Map<String, String>> {
        log.error(e.message, e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("message" to e.message))
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyTakenException::class)
    @ResponseBody
    fun handleEmailAlreadyTakenException(e: EmailAlreadyTakenException): ResponseEntity<Map<String, String>> {
        log.error(e.message, e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(mapOf("message" to (e.message ?: "internal error")))
    }

}