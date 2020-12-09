package com.altamar.shop.models.exсeptions;

import org.springframework.security.core.AuthenticationException;

public class TooMuchAuthAttemptsException extends AuthenticationException {

    public TooMuchAuthAttemptsException(String message) {
        super(message);
    }

    public TooMuchAuthAttemptsException(String message, Throwable cause) {
        super(message, cause);
    }

}
