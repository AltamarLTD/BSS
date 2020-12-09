package com.altamar.shop.models.dto.notification;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailServiceObject implements NotificationObject {

    private String recipient;

    private String sender;

    private String subject;

    private Message message;

}
