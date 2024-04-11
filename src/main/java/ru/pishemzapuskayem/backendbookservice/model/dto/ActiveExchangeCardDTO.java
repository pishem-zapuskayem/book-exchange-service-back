package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveExchangeCardDTO {
    private Long id;
    private MyOfferDTO myOffer;
    private OtherOfferDTO otherOffer;
    private UserExchangeStatusDTO myStatus;
    private UserExchangeStatusDTO otherStatus;
}