package com.altamar.shop.entity.user_management;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.models.dto.user_management.CartItemDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class CartItem implements ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer quantity;

    private Double amount;

    @OneToOne(orphanRemoval=true)
//    @Cascade(value = {org.hibernate.annotations.CascadeType.DELETE})
//    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;

    @Override
    public CartItemDTO toDto() {
        return CartItemDTO.of(quantity, amount, product.toDto());
    }

}