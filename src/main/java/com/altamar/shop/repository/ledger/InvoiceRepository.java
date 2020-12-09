package com.altamar.shop.repository.ledger;

import com.altamar.shop.entity.ledger.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("select inv from Invoice inv where inv.user.id = :userId")
    List<Invoice> getAllInvoicesByUserId(@Param("userId") Long userId);

}
