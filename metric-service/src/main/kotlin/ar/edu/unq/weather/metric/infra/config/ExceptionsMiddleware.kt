package ar.edu.unq.weather.metric.infra.config

import ar.edu.unq.weather.metric.domain.exceptions.*
import ar.edu.unq.weather.metric.infra.handlers.AvgLastDaySpringbootHandler
import io.github.resilience4j.bulkhead.BulkheadFullException
import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.*

@RestControllerAdvice
class ExceptionsMiddleware {
    private val log: Logger = LoggerFactory.getLogger(AvgLastDaySpringbootHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleNoDataFoundException(exception: NotFoundException): ResponseEntity<*> {
        log.warn("[REQUEST FAIL]:" + HttpStatus.NOT_FOUND.value() + HttpStatus.NOT_FOUND.reasonPhrase)
        return ResponseEntity<Any>(getBody(HttpStatus.NOT_FOUND, exception), HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(CallNotPermittedException::class)
    fun handleCallNotPermittedException(exception: CallNotPermittedException): ResponseEntity<*> {
        log.error("[REQUEST FAIL]:" + HttpStatus.SERVICE_UNAVAILABLE.value() + HttpStatus.SERVICE_UNAVAILABLE.reasonPhrase)
        return ResponseEntity<Any>(getBody(HttpStatus.SERVICE_UNAVAILABLE, exception), HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(ConnRefException::class, SocketException::class, SocketTimeoutException::class)
    fun handleConnRef(exception: ConnRefException): ResponseEntity<*> {
        log.error("[REQUEST FAIL]:" + HttpStatus.SERVICE_UNAVAILABLE.value() + HttpStatus.SERVICE_UNAVAILABLE.reasonPhrase)
        return ResponseEntity<Any>(getBody(HttpStatus.SERVICE_UNAVAILABLE, exception), HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE)
    }

    @ExceptionHandler(BulkheadFullException::class)
    fun handleTooManyRequests(exception: BulkheadFullException): ResponseEntity<*> {
        log.error("[REQUEST FAIL]:" + HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value() + HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.reasonPhrase)
        return ResponseEntity<Any>(getBody(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, exception), HttpHeaders(), HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
    }

    @ExceptionHandler(InfoBaeBadRequestError::class)
    fun handleBadReq(exception: InfoBaeBadRequestError): ResponseEntity<*> {
        log.warn("[REQUEST FAIL]:" + HttpStatus.INTERNAL_SERVER_ERROR.value() + HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
        return ResponseEntity<Any>(getBody(HttpStatus.BAD_REQUEST, exception), HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(RequestNotPermitted::class)
    fun handleTooMany(exception: RequestNotPermitted): ResponseEntity<*> {
        log.error("[REQUEST FAIL]:" + HttpStatus.TOO_MANY_REQUESTS.value() + HttpStatus.TOO_MANY_REQUESTS.reasonPhrase)
        return ResponseEntity<Any>(getBody(HttpStatus.TOO_MANY_REQUESTS, exception), HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS)
    }

    @ExceptionHandler(InfoBaeInternalServerError::class, InfoBaeException::class, RuntimeException::class)
    fun handleIntErr(exception: InfoBaeInternalServerError): ResponseEntity<*> {
        log.error("[REQUEST FAIL]:" + HttpStatus.INTERNAL_SERVER_ERROR.value() + HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase)
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