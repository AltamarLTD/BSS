package com.altamar.shop.models.dto.user_management;

import com.altamar.shop.entity.user_management.CartItem;
import com.altamar.shop.models.dto.Dto;
import com.altamar.shop.models.dto.product_catalog.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CartItemDTO implements Dto {

    @NotNull
    private Integer quantity;

    @NotNull
    private Double amount;

    @NotNull
    private ProductDTO productDTO;

    @Override
    public CartItem toEntity() {
        return CartItem.builder()
                .quantity(quantity)
                .amount(amount)
                .product(productDTO.toEntity())
                .build();
    }

}
