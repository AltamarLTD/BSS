package com.altamar.shop.service.internal.notification_old.senders;

import com.altamar.shop.models.dto.notification.EmailServiceObject;
import com.altamar.shop.models.dto.notification.Message;
import com.altamar.shop.service.external.MailSenderService;
import com.altamar.shop.service.internal.notification_old.provider.wrapper.SimpleNotificationProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailSender extends SimpleNotificationProvider<EmailServiceObject> {

    @Autowired
    private MailSenderService mailSenderService;


    @Override
    public String getNotificationType() {
        return "email";
    }

    @Override
    public void send(EmailServiceObject emailServiceObject) {
        Message message = super.prepareMessage();
        // some logic
        mailSenderService.sendEmail(emailServiceObject);
    }

}
