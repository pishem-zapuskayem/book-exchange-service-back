package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeCardDTO {
   private Long id;
   private MyOfferDTO exchangeMeDTO;
   private OtherOfferDTO exchangeGiveDTO;
}
