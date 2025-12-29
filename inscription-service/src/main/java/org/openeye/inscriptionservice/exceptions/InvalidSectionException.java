package org.openeye.inscriptionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSectionException extends RuntimeException {
    public InvalidSectionException(String message) {
        super(message);
    }
}
