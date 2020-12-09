package com.altamar.shop.models.dto.notification;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @NotNull
    @Size(min = 2, max = 20, message = "'name' size must be from 2 to 20 chars")
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 20, max = 100, message = "'message' size must be from 20 to 100 chars")
    private String msg;

}
