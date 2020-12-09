package com.altamar.shop.models.dto.user_management;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Checkout {

    private CartDTO cart;

    private UserDTO user;

}
