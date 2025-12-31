package org.openeye.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class KeycloakOperationException extends RuntimeException {
    public KeycloakOperationException(String message) {
        super(message);
    }

    public KeycloakOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
