package com.altamar.shop.models.exсeptions;

public class DeleteFileException extends RuntimeException {

    public DeleteFileException() {
        super("Delete file error.");
    }

    public DeleteFileException(String message) {
        super(message);
    }

}
