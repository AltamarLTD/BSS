package com.altamar.shop.service.internal.notification_old.senders;

import com.altamar.shop.models.dto.notification.Message;
import com.altamar.shop.models.dto.notification.TelegramServiceObject;
import com.altamar.shop.service.internal.notification_old.provider.wrapper.SimpleNotificationProvider;

public class TelegramSender extends SimpleNotificationProvider<TelegramServiceObject> {

    @Override
    public String getNotificationType() {
        return "telegram";  //// TODO: 02-Dec-20 Enum
    }

    @Override
    public void send(TelegramServiceObject notificationObject) {
        Message message = super.prepareMessage();
        //telegram service.send
    }

}
