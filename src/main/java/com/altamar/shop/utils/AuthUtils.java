package com.altamar.shop.utils;

import com.altamar.shop.entity.account_management.Account;
import com.altamar.shop.models.dto.aceess.AuthenticationPrincipal;
import com.altamar.shop.models.dto.aceess.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

public class AuthUtils {

    private static final String CLAIM_ROLES = "roles";
    private static final String SIGH_WITH = "ALTA";
    private static final int TIME_OF_EXPIRATION = 15;

    public static TokenDto generateToken(AuthenticationPrincipal principal) {
        try {
            if (principal == null || principal.getAccount() == null) {
                throw new IllegalAccessException("Authentication user should be specified to generate access token");
            }
            TokenDto token = buildToken(principal.getAccount());
            if (token == null) {
                throw new IllegalAccessException("Authentication token hasn't been generated...");
            }
            return token;
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to generate authentication token: " + e.getMessage());
        }
    }

    private static TokenDto buildToken(Account account) {
        if (account == null) return null;
        String token = Jwts.builder()
                .setSubject(account.getUsername())
                .claim(CLAIM_ROLES, account.getRole())
                .setIssuedAt(
                        new Date(System.currentTimeMillis())
                )
                .setExpiration(
                        new Date(Instant.now().plus(TIME_OF_EXPIRATION, ChronoUnit.MINUTES).toEpochMilli())
                )
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(SIGH_WITH.getBytes()))
                .compact();
        return TokenDto.builder()
                .token(token)
                .activeTill(getExpirationDateFromToken(token))
                .build();
    }

    private static Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(SIGH_WITH.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

}
