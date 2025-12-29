package org.openeye.sectionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTeacherException extends RuntimeException {
    public InvalidTeacherException(String message) {
        super(message);
    }
}
