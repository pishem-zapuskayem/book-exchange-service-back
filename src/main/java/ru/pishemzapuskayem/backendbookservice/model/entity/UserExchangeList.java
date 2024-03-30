package ru.pishemzapuskayem.backendbookservice.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Таблица хранит сведения по состоянию обмена " +
    "для каждого из участников (отправил свою книгу, получил другую)")
public class UserExchangeList extends AbstractEntity {
    @ManyToOne
    private ExchangeList exchangeList;
    @ManyToOne
    private  OfferList offerList;
    @Schema(description = "Номер трека отправления," +
        " вводится пользователем после отправки книги")
    private String trackNumber;
    @Schema(description = "Подтверждение получения книги от" +
        " другого пользователя")
    private Boolean receiving;
}
