package com.altamar.shop.repository.user_management;

import com.altamar.shop.entity.user_management.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByIp(String ip);

    boolean existsCartByIp(String ip);

}
