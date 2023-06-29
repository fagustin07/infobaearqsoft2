package org.arqsoft.WeatherLoader.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
public class TimeoutException extends Exception{
    public TimeoutException(String service) {
        super("Timeout exception for " + service + " service.");
    }
}
