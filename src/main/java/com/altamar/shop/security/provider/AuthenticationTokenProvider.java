package com.altamar.shop.security.provider;

import com.altamar.shop.models.dto.aceess.AuthenticationPrincipal;
import com.altamar.shop.security.token.AuthenticationBearerToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

@Slf4j
public abstract class AuthenticationTokenProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    private final UserDetailsChecker preAuthenticationChecks;
    private final UserDetailsChecker postAuthenticationChecks;
    private final GrantedAuthoritiesMapper authoritiesMapper;
    private MessageSourceAccessor messages;

    public AuthenticationTokenProvider() {
        this.messages = SpringSecurityMessageSource.getAccessor();
        this.preAuthenticationChecks = new DefaultPreAuthenticationChecks();
        this.postAuthenticationChecks = new DefaultPostAuthenticationChecks();
        this.authoritiesMapper = new NullAuthoritiesMapper();
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(
                AuthenticationBearerToken.class,
                authentication,
                () -> messages.getMessage(
                        "AbstractUserTokenAuthenticationProvider.onlySupports",
                        "Only BearerAuthenticationToken is supported"
                )
        );
        final AuthenticationBearerToken auth = (AuthenticationBearerToken) authentication;
        // determine token
        final String token = auth.getName();
        AuthenticationPrincipal authenticationPrincipal;

        try {
            authenticationPrincipal = retrieveAccountByToken(token);
        } catch (UsernameNotFoundException notFound) {
            log.debug("User not found");
            throw new BadCredentialsException(
                    messages.getMessage(
                            "AbstractUserTokenAuthenticationProvider.badCredentials",
                            "Bad credentials"
                    )
            );
        }
        Assert.notNull(
                authenticationPrincipal,
                "retrieveAccount returned null - a violation of the interface contract"
        );

        // check authentication
        preAuthenticationChecks.check(authenticationPrincipal);
        postAuthenticationChecks.check(authenticationPrincipal);
        if (!authenticationPrincipal.getAccount().getTokenIpAddress().equals(auth.getIp())) {
            throw new BadCredentialsException("Invalid user ip");
        }
        return createSuccessAuthentication(token, authenticationPrincipal, auth);
    }

    protected abstract AuthenticationPrincipal retrieveAccountByToken(String token) throws AuthenticationException;

    private Authentication createSuccessAuthentication(String token, AuthenticationPrincipal user, Authentication authentication) {
        final AuthenticationBearerToken result =
                new AuthenticationBearerToken(user, token, authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    public final void afterPropertiesSet() {
        Assert.notNull(this.messages, "A message source must be set");
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }


    public boolean supports(Class<?> authentication) {
        return (AuthenticationBearerToken.class.isAssignableFrom(authentication));
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                log.debug("User account is locked");
                throw new LockedException(
                        messages.getMessage(
                                "AbstractUserTokenAuthenticationProvider.locked",
                                "User account is locked"
                        )
                );
            }
            if (!user.isEnabled()) {
                log.debug("User account is disabled");
                throw new DisabledException(
                        messages.getMessage(
                                "AbstractUserTokenAuthenticationProvider.disabled",
                                "User is disabled"
                        )
                );
            }
            if (!user.isAccountNonExpired()) {
                log.debug("User token have expired");
                throw new AccountExpiredException(
                        messages.getMessage(
                                "AbstractUserTokenAuthenticationProvider.expired",
                                "User token have expired"
                        )
                );
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                log.debug("User token have expired");
                throw new CredentialsExpiredException(
                        messages.getMessage(
                                "AbstractUserTokenAuthenticationProvider.credentialsExpired",
                                "User token have expired"
                        )
                );
            }
        }
    }
}
