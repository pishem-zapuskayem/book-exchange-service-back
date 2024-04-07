package ru.pishemzapuskayem.backendbookservice.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Schema(description = "Таблица хранит сведения о предлагаеых пользователями (к обмену) книгах (ППК).")
public class OfferList extends AbstractEntity {
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "idBookLiterary")
    private BookLiterary bookLiterary;

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private Account user;

    private String isbn;
    private Integer yearPublishing;
    private LocalDateTime createdAt;

    @Schema(description = "состояние участия в обмене: свободен, отобран и т.п.")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @OneToMany
    private List<UserList> userLists;

    public OfferList setId(Long id) {
        super.setId(id);
        return this;
    }
}
