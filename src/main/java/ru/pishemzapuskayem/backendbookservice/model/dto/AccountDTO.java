package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String lastName;
    private String firstName;
    private String secondName;
    private String email;
    private String username;
    private String urlAvatar;
    private AccountAddressDTO address;
}
