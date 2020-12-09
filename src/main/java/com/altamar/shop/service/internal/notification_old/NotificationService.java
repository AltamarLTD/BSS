package com.altamar.shop.service.internal.notification_old;

import com.altamar.shop.models.dto.notification.NotificationObject;

import java.util.Map;

public interface NotificationService {

    void notify(Map<String, NotificationObject> map);

}
