package com.altamar.shop.models.dto.aceess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialDto {

    private String username;

    private String password;

}
