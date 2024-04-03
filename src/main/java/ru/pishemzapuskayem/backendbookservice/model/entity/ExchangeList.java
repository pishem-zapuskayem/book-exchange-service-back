package ru.pishemzapuskayem.backendbookservice.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Schema(description = """
    Таблица хранит сведения о книгах, которые система подобрала в пары для обмена.
    Примечание. После подтверждения “первым” пользователем согласия на обмен (кнопка “Меняюсь”
    или т.п.) в эту таблицу добавляется строка с данные по участникам, время - текущее. Если в
    течение 2 суток “второй” участник обмена не подтвердит участие (IsBoth == false), запись
    удаляем""")
public class ExchangeList extends AbstractEntity {
    @Schema(description = "Одна из книг на обмен (ППК)," +
        " ссылка на запись в таблице OfferList")
    @ManyToOne
    private OfferList firstOfferList;
    @Schema(description = "ЗПК первого пользователя," +
        " ссылка на запись в таблице WishList")
    @ManyToOne
    private WishList firstWishList;

    @Schema(description = "Другая из книг на обмен (ППК)," +
        " ссылка на запись в таблице OfferList")
    @ManyToOne
    private OfferList secondOfferList;
    @Schema(description = "ЗПК второго пользователя," +
        " ссылка на запись в таблице WishList")
    @ManyToOne
    private WishList secondWishList;
    @Schema(description = "Меняется на текущую при подтверждении каждым пользователем участия. Хранит дату, от которой будут отсчитываться периоды: -2 суток до получения подтверждения второго участника; - 7суток до " +
        "отправки книг участниками для случая, когда обмен подтвердили оба")
    private LocalDateTime createdAt;
    @Schema(description = "Когда запись добавляется = false." +
        " После того, как второй участник подтвердит = true.")
    private Boolean isBoth;
    @Schema(description = "Полное совпадение")
    private Boolean isFullMatch;
}
