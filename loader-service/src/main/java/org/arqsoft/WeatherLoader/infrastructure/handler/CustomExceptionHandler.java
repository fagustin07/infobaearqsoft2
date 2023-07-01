package org.arqsoft.WeatherLoader.infrastructure.handler;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class CustomExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoDataFoundException(NoDataFoundException exception) {
        logger.warn("[REQUEST FAIL]:" + NOT_FOUND.value() + NOT_FOUND.getReasonPhrase() );
        return new ResponseEntity<>(getBody(NOT_FOUND, exception), new HttpHeaders(), NOT_FOUND);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<Object> handleRequestNotPermittedException(RequestNotPermitted exception) {
        logger.warn("[REQUEST FAIL]:" + TOO_MANY_REQUESTS.value() + TOO_MANY_REQUESTS.getReasonPhrase() );
        return new ResponseEntity<>(getBody(TOO_MANY_REQUESTS, exception), new HttpHeaders(), TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        logger.error("[REQUEST FAIL]:" + INTERNAL_SERVER_ERROR.value() + INTERNAL_SERVER_ERROR.getReasonPhrase() );
        return new ResponseEntity<>(getBody(INTERNAL_SERVER_ERROR, exception), new HttpHeaders(), INTERNAL_SERVER_ERROR);
    }

    public Map<String, Object> getBody(HttpStatus status, Exception ex) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("exception", ex.toString());

        Throwable cause = ex.getCause();
        if (cause != null) {
            body.put("exceptionCause", ex.getCause().toString());
        }
        return body;
    }
}
