package com.altamar.shop.models.dto.user_management;

import com.altamar.shop.entity.user_management.User;
import com.altamar.shop.models.dto.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UserDTO implements Dto {

    private Long id;

    @NotNull
    @Size(min = 2, max = 20, message = "'name' size must be from 2 to 20 chars")
    private String name;

    @NotNull
    @Size(min = 2, max = 20, message = "'surname' size must be from 2 to 20 chars")
    private String surname;

    private String company;

    @NotNull
    @Size(min = 2, max = 20, message = "'country' size must be from 2 to 20 chars")
    private String country;

    @NotNull
    @Size(min = 2, max = 20, message = "'city' size must be from 2 to 20 chars")
    private String city;

    @NotNull
    @Size(min = 2, max = 20, message = "'region' size must be from 2 to 20 chars")
    private String region;

    @NotNull
    @Size(min = 10, max = 13, message = "'phone' size must be from 2 to 20 chars")
    private String phone;

    @NotNull
    @Email
    private String email;

    @Override
    public User toEntity() {
        return User.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .company(company)
                .country(country)
                .city(city)
                .region(region)
                .phone(phone)
                .email(email)
                .IPList(new ArrayList<>())
                .invoices(new ArrayList<>())
                .build();
    }

}
