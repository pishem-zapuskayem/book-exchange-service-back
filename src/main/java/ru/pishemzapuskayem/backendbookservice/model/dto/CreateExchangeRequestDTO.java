package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExchangeRequestDTO {
    private OfferDTO offer;
    private WishDTO wish;
    private AccountAddressDTO address;
    private Set<Long> offerCategoriesIds;
    private Set<Long> wishCategoriesIds;
}