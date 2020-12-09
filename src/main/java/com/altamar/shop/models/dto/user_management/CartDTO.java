package com.altamar.shop.models.dto.user_management;

import com.altamar.shop.entity.user_management.Cart;
import com.altamar.shop.models.dto.Dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CartDTO implements Dto {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private String ip;

    @NotNull
    private Double sum;

    @NotNull
    private Set<CartItemDTO> cartItems = new HashSet<>();

    @Override
    public Cart toEntity() {
        return Cart.builder()
                .id(id)
                .ip(ip)
                .sum(sum)
                .cartItems(cartItems.stream()
                        .filter(Objects::nonNull)
                        .map(CartItemDTO::toEntity)
                        .collect(Collectors.toSet()))
                .build();
    }

}
