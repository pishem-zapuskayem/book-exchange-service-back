package ru.pishemzapuskayem.backendbookservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDTO {
    private String lastName;
    private String firstName;
    private String secondName;
    @Schema(example = "vl7shab@yandex.ru")
    private String email;
    private String username;
    private String password;
    private AccountAddressDTO address;
}
