package com.altamar.shop.models.dto.ledger;

import com.altamar.shop.entity.ledger.Invoice;
import com.altamar.shop.models.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class InvoiceDTO implements Dto {

    private Long id;

    @NotNull
    private String date;

    @NotNull
    private Double sum;

    @NotNull
    private Set<InvoiceProductDTO> invoiceProductsDto = new HashSet<>();

    @NotNull
    private InvoiceStatusDTO invoiceStatusDTO;

    @Override
    public Invoice toEntity() {
        return Invoice.builder()
                .id(id)
                .date(date)
                .invoiceProducts(invoiceProductsDto.stream()
                        .filter(Objects::nonNull)
                        .map(InvoiceProductDTO::toEntity)
                        .collect(Collectors.toSet()))
                .invoiceStatus(invoiceStatusDTO.toEntity())
                .build();
    }

}
