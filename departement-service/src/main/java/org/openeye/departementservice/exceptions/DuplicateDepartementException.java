package org.openeye.departementservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateDepartementException extends RuntimeException {
    public DuplicateDepartementException(String message) {
        super(message);
    }
}
