package com.altamar.shop.repository.user_management;

import com.altamar.shop.entity.user_management.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    @Query("select ci from CartItem ci where ci.cart.id = :cartId and ci.product.id = :productId")
    Optional<CartItem> findByProduct_Id(Long cartId,
                                        Long productId);

    //// TODO: 03.12.20  chego blat?
    @Query("select case when count(c)> 0 then true else false end from CartItem c " +
            "where c.cart.id = :cartId and c.product.id = :productId")
    boolean existsByProduct_IdAndCart_Id(@Param("productId") Long productId,
                                         @Param("cartId") Long cartId);

    @Modifying
    @Query("delete from CartItem where cart.id = :id")
    void deleteAllByCart_Id(@Param("id") Long id);

    void deleteById(Long id);

}
