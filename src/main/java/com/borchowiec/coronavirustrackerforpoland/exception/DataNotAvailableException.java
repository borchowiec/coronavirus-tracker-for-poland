package com.borchowiec.coronavirustrackerforpoland.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Should be thrown when data is not currently available. Returns {@link HttpStatus#NO_CONTENT}.
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class DataNotAvailableException extends RuntimeException {
    public DataNotAvailableException() {
        super("Currently data is no available. Try later.");
    }

    public DataNotAvailableException(String message) {
        super(message);
    }
}
