package com.altamar.shop.models.dto.account_management;

import com.altamar.shop.entity.account_management.Account;
import com.altamar.shop.models.dto.Dto;
import com.altamar.shop.models.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AccountDTO implements Dto {

    @NotNull
    @Size(min = 5, max = 20, message = "'username' size must be from 5 to 20 chars")
    private String username;

    @NotNull
    @Size(min = 5, max = 20, message = "'password' size must be from 5 to 20 chars")
    private String password;

    @NotNull
    private Roles role;

    @Override
    public Account toEntity() {
        return Account.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();
    }

}
