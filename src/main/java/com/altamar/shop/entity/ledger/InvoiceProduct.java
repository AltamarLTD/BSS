package com.altamar.shop.entity.ledger;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.entity.product_catalog.Product;
import com.altamar.shop.models.dto.ledger.InvoiceProductDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "invoice")
public class InvoiceProduct implements ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonManagedReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private Invoice invoice;

    @Override
    public InvoiceProductDTO toDto() {
        return InvoiceProductDTO.of(quantity, product.toDto());
    }

}
