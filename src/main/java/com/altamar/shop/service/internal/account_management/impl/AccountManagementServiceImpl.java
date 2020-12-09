package com.altamar.shop.service.internal.account_management.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class AccountManagementServiceImpl {

    public AccountManagementServiceImpl() {
    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : AccountManagementServiceImpl have been initialized");
    }
}
