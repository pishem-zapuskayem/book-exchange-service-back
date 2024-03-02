package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDTO {
    private String lastName;
    private String firstName;
    private String secondName;
    private String email;
    private String username;
    private String password;
    private AccountAddressDTO address;
}
