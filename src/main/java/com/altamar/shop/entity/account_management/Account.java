package com.altamar.shop.entity.account_management;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.models.dto.account_management.AccountDTO;
import com.altamar.shop.models.enums.Roles;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String token;

    private Integer passRetryNumber;

    private Date tokenExpirationDate;

    private String tokenIpAddress;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Override
    public AccountDTO toDto() {
        return AccountDTO.of(username, password, role);
    }

}
