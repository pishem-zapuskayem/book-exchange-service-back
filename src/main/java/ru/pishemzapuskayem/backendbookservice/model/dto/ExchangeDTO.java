package ru.pishemzapuskayem.backendbookservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

//todo рейтинг нужен ещё
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDTO {
    private Long id;
    private BookDTO offerFirst;
    private BookDTO offerSecond;
    private Boolean isFullMatch;
    private Status status;
}
