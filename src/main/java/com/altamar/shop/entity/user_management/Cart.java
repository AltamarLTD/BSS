package com.altamar.shop.entity.user_management;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.models.dto.user_management.CartDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ip;

    private Double sum;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CartItem> cartItems = new HashSet<>();

    public void addCartItem(CartItem cartItem) {
        if (!cartItems.contains(cartItem)) {
            cartItems.add(cartItem);
            cartItem.setCart(this);
        }
    }

    public void deleteCartItem(CartItem cartItem) {
        if (cartItems.contains(cartItem)) {
            cartItems.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void deleteAllCartItems() {
        this.cartItems.clear();
    }

    @Override
    public CartDTO toDto() {
        return CartDTO.of(id, ip, sum, cartItems.stream()
                .map(CartItem::toDto)
                .collect(Collectors.toSet()));
    }

}
