package com.altamar.shop.security.token;

import com.altamar.shop.models.dto.aceess.AuthenticationPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

@Slf4j
public class AuthenticationBearerToken extends AbstractAuthenticationToken {

    private AuthenticationPrincipal principal;
    private String token;
    private String ip;

    public AuthenticationBearerToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public AuthenticationBearerToken(String token, String ip) {
        super(null);
        this.token = Objects.requireNonNull(token);
        this.ip = Objects.requireNonNull(ip);
        setAuthenticated(false);
    }

    public AuthenticationBearerToken(AuthenticationPrincipal principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = Objects.requireNonNull(principal);
        this.token = Objects.requireNonNull(token);
        super.setAuthenticated(true);
    }

    public String getCredentials() {
        return this.token;
    }

    @Override
    public String getName() {
        return Objects.toString(this.token);
    }

    public AuthenticationPrincipal getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    public String getIp() {
        return ip;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.token = null;
    }
}
