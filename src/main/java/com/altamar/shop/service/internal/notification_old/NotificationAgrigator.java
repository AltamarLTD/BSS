package com.altamar.shop.service.internal.notification_old;

import com.altamar.shop.service.internal.notification_old.provider.NotifierProvider;
import com.altamar.shop.service.internal.notification_old.senders.EmailSender;
import com.altamar.shop.service.internal.notification_old.senders.TelegramSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class NotificationAgrigator {

    @Autowired
    TelegramSender telegramSender;
    @Autowired
    EmailSender emailSender;

    Map<String, NotifierProvider> map = new HashMap<>();

    {
        map.put(telegramSender.getNotificationType(), telegramSender);
        map.put(emailSender.getNotificationType(), emailSender);
    }

    public NotifierProvider getProviderByType(String type) {
        return map.get(type);
    }

}
