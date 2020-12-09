package com.altamar.shop.config;

import com.altamar.shop.repository.account_management.AccountRepository;
import com.altamar.shop.security.access.AuthenticationTokenEntryPoint;
import com.altamar.shop.security.filter.AuthenticationFilter;
import com.altamar.shop.security.handler.AuthenticationTokenSuccessHandler;
import com.altamar.shop.security.provider.AuthenticationProvider;
import com.altamar.shop.service.internal.authorization.AuthorizationService;
import com.altamar.shop.service.internal.authorization.impl.AuthorizationServiceImpl;
import com.altamar.shop.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

@Slf4j
@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AccountRepository accountRepository;

    public SecurityConfiguration(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/api/v2/access/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(SecurityUtils.getProtectedURLs())
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(tokenEntryPoint())
                .and()
                .addFilterBefore(new CorsFilterConfiguration(), ChannelProcessingFilter.class)
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    public AuthorizationService authorizationService() {
        return new AuthorizationServiceImpl(accountRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    protected AuthenticationProvider authenticationProvider() {
        return new AuthenticationProvider(authorizationService());
    }

    @Bean
    protected AuthenticationFilter authenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(SecurityUtils.getProtectedURLs());
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(tokenSuccessHandler());
        return filter;
    }

    @Bean
    protected AuthenticationSuccessHandler tokenSuccessHandler() {
        return new AuthenticationTokenSuccessHandler();
    }


    @Bean
    protected AuthenticationEntryPoint tokenEntryPoint() {
        return new AuthenticationTokenEntryPoint();
    }

}
