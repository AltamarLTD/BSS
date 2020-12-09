package com.altamar.shop.security.filter;

import com.altamar.shop.security.token.AuthenticationBearerToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String BEARER = "Bearer";

    public AuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse) throws AuthenticationException {
        logger.debug("Authentication [Filter]: Attempt");
        final Optional<String> tokenParam = Optional.ofNullable(httpServletRequest.getHeader(AUTHORIZATION));
        final String bearerToken = tokenParam.orElseThrow(
                (Supplier<AuthenticationException>) () ->
                        new BadCredentialsException("Token is not provided")
        );
        final String token = StringUtils.removeStart(bearerToken, BEARER).trim();
        final String ip = httpServletRequest.getRemoteAddr();
        final Authentication requestAuthentication = new AuthenticationBearerToken(token, ip);
        return getAuthenticationManager().authenticate(requestAuthentication);
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult
    ) throws IOException, ServletException {
        logger.debug("Authentication [Filter]: Sttempt");
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
