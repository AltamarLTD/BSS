package com.altamar.shop.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Roles {

    ADMIN("ADMIN");

    private final String name;

    public String value() {
        return "ROLE_" + name;
    }

}
