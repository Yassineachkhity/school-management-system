package org.openeye.inscriptionservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InscriptionNotFoundException extends RuntimeException {
    public InscriptionNotFoundException(String message) {
        super(message);
    }
}
