package com.altamar.shop.service.external.impl;

import com.altamar.shop.models.dto.notification.EmailServiceObject;
import com.altamar.shop.models.exсeptions.MailSenderException;
import com.altamar.shop.service.external.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class MailSenderServiceImpl implements MailSenderService {

    private static final String X_RAPIDAPI_KEY = "x-rapidapi-key";
    private static final String X_RAPIDAPI_HOST = "x-rapidapi-host";
    private static final String FAPIMAIL_P_RAPIDAPI_COM = "fapimail.p.rapidapi.com";

    @Value("${rest.email.service.url}")
    private String URL;

    @Value("${rest.email.service.access.token}")
    private String accessToken;

    public MailSenderServiceImpl() {

    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : MailSenderServiceImpl have been initialized with param {}", URL);
    }

    /**
     * This method though headers address to rest Email Service with access token
     * Transfer emailServiceObject in json to rest Email Service
     *
     * @param emailServiceObject (recipient, sender, subject, message)
     * @throws MailSenderException if email notification was not send
     */
    @Override
    public void sendEmail(EmailServiceObject emailServiceObject) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();

        headers.set(X_RAPIDAPI_HOST, FAPIMAIL_P_RAPIDAPI_COM);
        headers.set(X_RAPIDAPI_KEY, accessToken);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        try {
            final HttpEntity<EmailServiceObject> entity = new HttpEntity<>(emailServiceObject, headers);
            log.info("[MailSenderServiceImpl] : Sending request to rest api by url {}", URL);
            restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
            log.info("[MailSenderServiceImpl] : Sent request to rest api by url {}", URL);
        } catch (Exception e) {
            throw new MailSenderException("Email notification was not send.");
        }
    }

}
