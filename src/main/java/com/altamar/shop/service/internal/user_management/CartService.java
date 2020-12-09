package com.altamar.shop.service.internal.user_management;

import com.altamar.shop.models.dto.user_management.CartDTO;

public interface CartService {

    CartDTO createCart(String ip);

    CartDTO getCart(String ip);

    CartDTO addToCart(String ip, Long productId);

    CartDTO updateCart(String ip, Long productId, int quantity);

    CartDTO deleteItem(String ip, Long productId);

    CartDTO deleteAllItems(String ip);

    void deleteCartById(Long cartId);

}
