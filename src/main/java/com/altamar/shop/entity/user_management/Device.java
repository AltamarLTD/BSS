package com.altamar.shop.entity.user_management;

import com.altamar.shop.entity.ApplicationEntity;
import com.altamar.shop.models.dto.user_management.DeviceDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device implements ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ip;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Override
    public DeviceDTO toDto() {
        return DeviceDTO.of(ip, user.toDto());
    }

}
