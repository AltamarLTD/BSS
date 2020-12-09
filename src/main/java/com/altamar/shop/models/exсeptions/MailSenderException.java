package com.altamar.shop.models.exсeptions;

public class MailSenderException extends RuntimeException {

    public MailSenderException() {
        super("Notification send exception.");
    }

    public MailSenderException(String message) {
        super(message);
    }

}
