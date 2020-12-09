package com.altamar.shop.service.internal.notification_old.provider.wrapper;

import com.altamar.shop.models.dto.notification.Message;
import com.altamar.shop.models.dto.notification.NotificationObject;
import com.altamar.shop.service.internal.notification_old.provider.NotifierProvider;

public abstract class SimpleNotificationProvider<T extends NotificationObject> implements NotifierProvider<T> {

    @Override
    public Message prepareMessage() {
        return null;
    }

}
