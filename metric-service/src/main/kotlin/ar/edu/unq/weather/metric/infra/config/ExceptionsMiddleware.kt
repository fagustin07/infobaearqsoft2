package ar.edu.unq.weather.metric.infra.config

import ar.edu.unq.weather.metric.domain.exceptions.*
import io.github.resilience4j.bulkhead.BulkheadFullException
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice
class ExceptionsMiddleware {

    @ExceptionHandler(NotFoundException::class)
    fun handleNoDataFoundException(exception: NotFoundException): ResponseEntity<*> {
        return ResponseEntity<Any>(getBody(HttpStatus.NOT_FOUND, exception), HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(CallNotPermittedException::class)
    fun handleCallNotPermittedException(exception: CallNotPermittedException): ResponseEntity<*> {
        return ResponseEntity<Any>(getBody(HttpStatus.SERVICE_UNAVAILABLE, exception), HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(ConnRefException::class)
    fun handleConnRef(exception: ConnRefException): ResponseEntity<*> {
        return ResponseEntity<Any>(getBody(HttpStatus.SERVICE_UNAVAILABLE, exception), HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(BulkheadFullException::class)
    fun handleTooManyRequests(exception: BulkheadFullException): ResponseEntity<*> {
        return ResponseEntity<Any>(getBody(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, exception), HttpHeaders(), HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    }

    @ExceptionHandler(InfoBaeBadRequestError::class)
    fun handleBadReq(exception: InfoBaeBadRequestError): ResponseEntity<*> {
        return ResponseEntity<Any>(getBody(HttpStatus.BAD_REQUEST, exception), HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InfoBaeInternalServerError::class, InfoBaeException::class, RuntimeException::class)
    fun handleIntErr(exception: InfoBaeInternalServerError): ResponseEntity<*> {
        return ResponseEntity<Any>(getBody(HttpStatus.INTERNAL_SERVER_ERROR, exception), HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    fun getBody(status: HttpStatus, ex: Exception): Map<String, Any>? {
        val body: MutableMap<String, Any> = LinkedHashMap()
        body["timestamp"] = Date()
        body["status"] = status.value()
        body["error"] = status.reasonPhrase
        body["exception"] = ex::class.java.simpleName
        body["message"] = ex.message ?: "NO_SPECIFIED"

        return body
    }
}