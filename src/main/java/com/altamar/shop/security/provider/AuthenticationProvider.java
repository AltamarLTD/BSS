package com.altamar.shop.security.provider;


import com.altamar.shop.entity.account_management.Account;
import com.altamar.shop.models.dto.aceess.AuthenticationPrincipal;
import com.altamar.shop.service.internal.authorization.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class AuthenticationProvider extends AuthenticationTokenProvider {

    private final AuthorizationService authorizationService;

    public AuthenticationProvider(AuthorizationService authorizationService) {
        super();
        this.authorizationService = Objects.requireNonNull(authorizationService);
    }

    @Override
    protected AuthenticationPrincipal retrieveAccountByToken(String token) throws AuthenticationException {
        log.debug("Authentication [Provider]: Retrieve user principal by authentication token: " + token);
        final Account systemUser = Optional
                .ofNullable(token)
                .map(String::valueOf)
                .flatMap(authorizationService::getAccountByToken)
                .map(Account.class::cast)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find account with authentication token"));
        return new AuthenticationPrincipal(systemUser);
    }
}
