package com.altamar.shop.service.external;

import com.altamar.shop.models.dto.notification.EmailServiceObject;

public interface MailSenderService {

    void sendEmail(EmailServiceObject emailServiceObject);

}
