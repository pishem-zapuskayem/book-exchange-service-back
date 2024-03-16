package ru.pishemzapuskayem.backendbookservice.model.entity.message;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.pishemzapuskayem.backendbookservice.model.entity.AbstractEntity;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;

import java.time.LocalDate;

@Entity
@Table(name = "UserMsg")
@Getter
@Setter
@Accessors(chain = true)
public class UserMessage extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "IdUser", nullable = false)
    private Account user;

    @Column(name = "CreateAt", nullable = false)
    private LocalDate createdAt;

    @Column(name = "Text", nullable = false)
    private String text;

    @Column(name = "Notes", length = 150)
    private String notes;

    @Column(name = "IdStatus", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "Type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private MessageType type;
}
