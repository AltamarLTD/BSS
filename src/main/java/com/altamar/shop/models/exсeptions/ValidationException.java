package com.altamar.shop.models.exсeptions;

public class ValidationException extends RuntimeException {

    public ValidationException() {
        super("Validation exception.");
    }

    public ValidationException(String message) {
        super(message);
    }

}
