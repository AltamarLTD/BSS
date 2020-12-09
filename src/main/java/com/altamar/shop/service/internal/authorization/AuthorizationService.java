package com.altamar.shop.service.internal.authorization;



import com.altamar.shop.entity.account_management.Account;
import com.altamar.shop.models.dto.aceess.CredentialDto;
import com.altamar.shop.models.dto.aceess.TokenDto;

import java.util.Optional;

public interface AuthorizationService {

    TokenDto authorize(CredentialDto credential, String ip);

    Optional<Account> getAccountByToken(String token);

}
