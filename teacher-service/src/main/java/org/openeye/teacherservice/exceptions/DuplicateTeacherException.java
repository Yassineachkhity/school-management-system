package org.openeye.teacherservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateTeacherException extends RuntimeException {
    public DuplicateTeacherException(String message) {
        super(message);
    }
}
