package org.arqsoft.WeatherLoader.infrastructure.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.arqsoft.WeatherLoader.domain.exceptions.NoDataFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNoDataFoundException(NoDataFoundException exception) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, exception), new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(getBody(BAD_REQUEST, exception), new HttpHeaders(), BAD_REQUEST);
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
