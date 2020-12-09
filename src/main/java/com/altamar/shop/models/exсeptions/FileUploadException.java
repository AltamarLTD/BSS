package com.altamar.shop.models.exсeptions;

public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super("Error load file. File was not uploaded.");
    }

    public FileUploadException(String message) {
        super(message);
    }

}
