package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountAddressDTO {
    private Integer addrIndex;
    private String addrCity;
    private String addrStreet;
    private String addrBuilding;
    private String addrApartment;
}
