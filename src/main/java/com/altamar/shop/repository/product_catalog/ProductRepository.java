package com.altamar.shop.repository.product_catalog;

import com.altamar.shop.entity.product_catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsById(Long id);

    Optional<Product> findByName(String name);

}
