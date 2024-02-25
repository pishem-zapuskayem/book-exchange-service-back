package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDTO {
    private String lastname;
    private String name;
    private String patronymic;
    private String email;
    private String nickname;
    private String password;
    private AddressDTO address;

}
