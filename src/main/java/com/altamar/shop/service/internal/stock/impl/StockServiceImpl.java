package com.altamar.shop.service.internal.stock.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class StockServiceImpl {

    public StockServiceImpl() {
    }

    @PostConstruct
    public void init() {
        log.info("[WebConfiguration] : FileServiceImpl have been initialized");
    }

}
