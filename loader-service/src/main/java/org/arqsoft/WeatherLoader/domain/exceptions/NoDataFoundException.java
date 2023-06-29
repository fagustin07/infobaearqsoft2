package org.arqsoft.WeatherLoader.domain.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoDataFoundException extends Exception {
    public NoDataFoundException(String location) {
        super("No data found for " + location + ".");
    }
}
