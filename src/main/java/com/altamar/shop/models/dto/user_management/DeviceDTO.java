package com.altamar.shop.models.dto.user_management;

import com.altamar.shop.entity.user_management.Device;
import com.altamar.shop.models.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class DeviceDTO implements Dto {

    @NotNull
    private String ip;

    @NotNull
    private UserDTO userDTO;

    @Override
    public Device toEntity() {
        return Device.builder()
                .ip(ip)
                .user(userDTO.toEntity())
                .build();
    }

}
