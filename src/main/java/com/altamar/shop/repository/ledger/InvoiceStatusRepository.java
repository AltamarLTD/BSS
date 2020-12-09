package com.altamar.shop.repository.ledger;

import com.altamar.shop.entity.ledger.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceStatusRepository extends JpaRepository<InvoiceStatus, Long> {

    Optional<InvoiceStatus> findByStatus(String status);

}
