package com.altamar.shop.config;

import com.altamar.shop.entity.account_management.Account;
import com.altamar.shop.entity.ledger.InvoiceStatus;
import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.models.enums.Roles;
import com.altamar.shop.repository.account_management.AccountRepository;
import com.altamar.shop.repository.ledger.InvoiceStatusRepository;
import com.altamar.shop.repository.product_catalog.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class WebConfiguration {

    @Bean
    public CommandLineRunner demo(final InvoiceStatusRepository invoiceStatusRepository, final ProductRepository productRepository, final AccountRepository accountRepository) {
        return strings -> {
            invoiceStatusRepository.save(InvoiceStatus.builder().status("IN PROCESS").build());
            invoiceStatusRepository.save(InvoiceStatus.builder().status("PAYED").build());
            invoiceStatusRepository.save(InvoiceStatus.builder().status("CANCELED").build());
            productRepository.save(Product.builder()
                    .name("xek")
                    .country("USA")
                    .date("12.01.2020")
                    .description("dfdfdsfdsfdsfsd")
                    .pack("30kg")
                    .priceKg(15.0)
                    .pricePack(450.0)
                    .build());
            productRepository.save(Product.builder()
                    .name("vomer")
                    .country("Finland")
                    .date("15.01.2020")
                    .description("dfdfdsfdsfdsfsddfgfdg")
                    .pack("20kg")
                    .priceKg(15.0)
                    .pricePack(300.0)
                    .build());
            accountRepository.save(Account.builder()
                    .username("admin")
                    .password("$2y$10$T1Ol7.pqvlr7HdZj9k8TL.IOfdFirDLH.CMfbMav0.A3NVCvdz8dG")
                    .role(Roles.ADMIN)
                    .build());
        };
    }

}
