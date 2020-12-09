package com.altamar.shop.utils;

import com.altamar.shop.entity.user_management.Cart;
import com.altamar.shop.entity.user_management.CartItem;

public class CartUtils {

    /**
     * This method get all CartItems from Cart from argument
     * In cycle from each carItem get amount then add to the total on each iteration
     * Set variable total to the cart sum
     *
     * @param cart (ip, sum, cartItems)
     */
    public static void calculateSumOfCart(Cart cart) {
        double total = 0;
        for (CartItem ci : cart.getCartItems()) {
            total += ci.getAmount();
        }
        cart.setSum(total);
    }

}
