package com.altamar.shop.models.dto.ledger;

import com.altamar.shop.entity.ledger.InvoiceStatus;
import com.altamar.shop.models.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class InvoiceStatusDTO implements Dto {

    @NotNull
    @Size(min = 5, max = 20, message = "'status' size must be from 5 to 20 chars")
    private String status;

    @Override
    public InvoiceStatus toEntity() {
        return InvoiceStatus.builder()
                .status(status)
                .build();
    }

}
