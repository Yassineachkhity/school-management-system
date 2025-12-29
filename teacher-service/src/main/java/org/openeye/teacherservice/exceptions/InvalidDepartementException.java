package org.openeye.teacherservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDepartementException extends RuntimeException {
    public InvalidDepartementException(String message) {
        super(message);
    }
}
