package com.altamar.shop.repository.user_management;

import com.altamar.shop.entity.user_management.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u inner join Invoice as i on u.id = i.user.id and i.id = :invoiceId")
    User getUserByInvoiceId(@Param("invoiceId") Long invoiceId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);

}
