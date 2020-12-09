package com.altamar.shop.service.internal.authorization.impl;

import com.altamar.shop.entity.account_management.Account;
import com.altamar.shop.models.dto.aceess.AuthenticationPrincipal;
import com.altamar.shop.models.dto.aceess.CredentialDto;
import com.altamar.shop.models.dto.aceess.TokenDto;
import com.altamar.shop.models.exсeptions.NotFoundException;
import com.altamar.shop.models.exсeptions.TooMuchAuthAttemptsException;
import com.altamar.shop.repository.account_management.AccountRepository;
import com.altamar.shop.service.internal.authorization.AuthorizationService;
import com.altamar.shop.utils.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {


    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TokenDto authorize(CredentialDto credential, String ip) {
        // check credential
        if (credential == null || StringUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("Authentication credentials should be specified");
        }
        // define attributes
        final String username = credential.getUsername();
        final String password = credential.getPassword();
        // check credential
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new IllegalArgumentException("Username or password is missing");
        }
        final Optional<Account> accountOptional = accountRepository.findByUsername(username);
        // check system user
        if (!accountOptional.isPresent()) {
            throw new NotFoundException("Account not found");
        }
        // check password
        final Account account = accountOptional.get();
        boolean isSamePassword = checkPassword(password, account);
        if (!isSamePassword) {
            getAddPasswordAttempt(account);
            throw new BadCredentialsException("Wrong password");
        } else {
            account.setPassRetryNumber(0);
        }
        final AuthenticationPrincipal principal = new AuthenticationPrincipal(account);
        try {
            TokenDto token = AuthUtils.generateToken(principal);
            account.setToken(token.getToken());
            account.setTokenExpirationDate(token.getActiveTill());
            account.setTokenIpAddress(ip);
            final Account savedAccount = accountRepository.save(account);
            if (savedAccount != null) {
                return TokenDto.builder()
                        .token(savedAccount.getToken())
                        .activeTill(savedAccount.getTokenExpirationDate())
                        .build();
            } else {
                throw new UnknownError();
            }
        } catch (Exception e) {
            throw new SecurityException(e.getMessage());
        }
    }

    @Override
    public Optional<Account> getAccountByToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException("Token is not specified");
        }
        return accountRepository.findByToken(token);
    }

    private void getAddPasswordAttempt(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account should be specified to increment password attempt");
        }
        int retryNumber = account.getPassRetryNumber() == null ? 1 : account.getPassRetryNumber() + 1;
        account.setPassRetryNumber(retryNumber);
        accountRepository.save(account);
    }

    private boolean checkPassword(String inputPassword, Account account) {
        if (account.getPassRetryNumber() != null && account.getPassRetryNumber().compareTo(5) >= 0) {
            throw new TooMuchAuthAttemptsException("Too Much Authentication Attempts. Try to contact tech support");
        }
        log.info(inputPassword);
        log.info(account.getPassword());
        if (passwordEncoder == null) {
            return inputPassword.equals(account.getPassword());
        } else {

            return passwordEncoder.matches(inputPassword, account.getPassword());
        }
    }
}
