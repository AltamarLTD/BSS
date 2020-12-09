package com.altamar.shop.entity.ledger;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.entity.user_management.User;
import com.altamar.shop.models.dto.ledger.InvoiceDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Invoice implements ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String date;

    private Double sum;

    private String msg;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<InvoiceProduct> invoiceProducts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "invoice_status_id")
    @JsonManagedReference
    private InvoiceStatus invoiceStatus;

    public void addInvoiceProduct(InvoiceProduct invoiceProduct) {
        if (!invoiceProducts.contains(invoiceProduct)) {
            invoiceProducts.add(invoiceProduct);
            invoiceProduct.setInvoice(this);
        }
    }

    @Override
    public InvoiceDTO toDto() {
        return InvoiceDTO.of(id, date, sum, invoiceProducts.stream()
                        .filter(Objects::nonNull)
                        .map(InvoiceProduct::toDto)
                        .collect(Collectors.toSet()),
                invoiceStatus.toDto());
    }

}

