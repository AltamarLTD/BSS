package com.altamar.shop.entity.ledger;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.models.dto.ledger.InvoiceStatusDTO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceStatus implements ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String status;

    @Override
    public InvoiceStatusDTO toDto() {
        return InvoiceStatusDTO.of(status);
    }

}
