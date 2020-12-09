package com.altamar.shop.service.internal.notification_old.provider;

import com.altamar.shop.models.dto.notification.Message;
import com.altamar.shop.models.dto.notification.NotificationObject;

public interface NotifierProvider<T extends NotificationObject> {

     String getNotificationType();

     Message prepareMessage();

    void send(T t);

}
