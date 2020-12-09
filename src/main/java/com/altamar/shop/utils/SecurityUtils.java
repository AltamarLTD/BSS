package com.altamar.shop.utils;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SecurityUtils {

    public static RequestMatcher getProtectedURLs() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/admin/**")
        );
    }
}
