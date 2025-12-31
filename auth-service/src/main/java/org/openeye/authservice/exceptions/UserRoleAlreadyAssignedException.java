package org.openeye.authservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserRoleAlreadyAssignedException extends RuntimeException {
    public UserRoleAlreadyAssignedException(String message) {
        super(message);
    }
}
