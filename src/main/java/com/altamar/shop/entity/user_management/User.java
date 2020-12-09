package com.altamar.shop.entity.user_management;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.entity.ledger.Invoice;
import com.altamar.shop.models.dto.user_management.UserDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String surname;

    private String company;

    private String country;

    private String city;

    private String region;

    private String phone;

    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Device> IPList = new ArrayList<>();

    public void addInvoice(Invoice invoice) {
        if (!invoices.contains(invoice)) {
            invoices.add(invoice);
            invoice.setUser(this);
        }
    }

    public void addIP(Device device) {
        if (!IPList.contains(device)) {
            IPList.add(device);
            device.setUser(this);
        }
    }

    @Override
    public UserDTO toDto() {
        return UserDTO.of(id, name, surname, company, country, city, region, phone, email);
    }

}
