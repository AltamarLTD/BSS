package com.altamar.shop.service.internal.notification_old.impl;

import com.altamar.shop.models.dto.notification.NotificationObject;
import com.altamar.shop.service.internal.notification_old.NotificationAgrigator;
import com.altamar.shop.service.internal.notification_old.NotificationService;
import com.altamar.shop.service.internal.notification_old.provider.NotifierProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationAgrigator notificationAgrigator;

    @Override
    public void notify(Map<String, NotificationObject> map) {

        map.keySet().forEach(key -> {
            NotifierProvider provider = notificationAgrigator.getProviderByType(key);
            NotificationObject notificationObject = map.get(key);
            provider.send(notificationObject);
        });

    }

}
