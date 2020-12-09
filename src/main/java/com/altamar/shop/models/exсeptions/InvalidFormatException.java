package com.altamar.shop.models.exсeptions;

public class InvalidFormatException extends RuntimeException {

    public InvalidFormatException() {
        super("Invalid file format.");
    }

    public InvalidFormatException(String message) {
        super(message);
    }

}
