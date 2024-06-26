package ru.pishemzapuskayem.backendbookservice.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.pishemzapuskayem.backendbookservice.converters.StatusConverter;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class WishList extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private Account user;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Schema(description = "состояние участия в обмене: свободен, отобран и т.п.")
    @Convert(converter = StatusConverter.class)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "idUserAddress", referencedColumnName = "id", nullable = false)
    private AccountAddress address;

    @OneToMany
    private List<UserList> userLists;

    public WishList setId(Long id) {
        super.setId(id);
        return this;
    }
}
