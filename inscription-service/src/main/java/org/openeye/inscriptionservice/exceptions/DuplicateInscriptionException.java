package org.openeye.inscriptionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateInscriptionException extends RuntimeException {
    public DuplicateInscriptionException(String message) {
        super(message);
    }
}
