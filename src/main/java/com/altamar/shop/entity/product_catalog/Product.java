package com.altamar.shop.entity.product_catalog;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.entity.ledger.InvoiceProduct;
import com.altamar.shop.entity.user_management.CartItem;
import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class Product implements ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String country;

    private String pack;

    private String date;

    private Double priceKg;

    private Double pricePack;

    private String imgCode = "DEFAULT.jpg";

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @Override
    public ProductDTO toDto() {
        return ProductDTO.of(id, name, country, pack, date, priceKg, pricePack, imgCode, description);
    }

}
