package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExchangeRequestDTO {
    private OfferDTO offer;
    private List<CategoryDTO> offerCategories;
    private WishDTO wish;
    private List<CategoryDTO> wishCategories;
    private AccountAddressDTO address;
}