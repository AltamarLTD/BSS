package com.altamar.shop.models.dto.ledger;

import com.altamar.shop.entity.ledger.InvoiceProduct;
import com.altamar.shop.models.dto.Dto;
import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class InvoiceProductDTO implements Dto {

    private Integer quantity;

    private ProductDTO productDTO;

    @Override
    public InvoiceProduct toEntity() {
        return InvoiceProduct.builder()
                .quantity(quantity)
                .product(productDTO.toEntity())
                .build();
    }

}
