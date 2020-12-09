package com.altamar.shop.models.dto.product_catalog;

import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.models.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ProductDTO implements Dto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 50, message = "'name' size must be from 2 to 50 chars")
    private String name;

    @NotNull
    @Size(min = 2, max = 50, message = "'country' size must be from 2 to 50 chars")
    private String country;

    @NotNull
    @Size(min = 2, max = 20, message = "'pack' size must be from 2 to 20 chars")
    private String pack;

    @NotNull
    @Size(min = 2, max = 20, message = "'date' size must be from 2 to 20 chars")
    private String date;

    @NotNull
    private Double priceKg;

    @NotNull
    private Double pricePack;

    private String img = "DEFAULT.jpg";

    @NotNull
    @Size(min = 10, max = 2000, message = "'description' size must be from 10 to 2000 chars")
    private String description;

    @Override
    public Product toEntity() {
        return Product.builder()
                .id(id)
                .name(name)
                .country(country)
                .pack(pack)
                .date(date)
                .priceKg(priceKg)
                .pricePack(pricePack)
                .imgCode(img)
                .description(description)
                .build();
    }

}