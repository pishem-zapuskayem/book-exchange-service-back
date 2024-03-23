package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class OfferList extends AbstractEntity {
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "idBookLiterary")
    private BookLiterary bookLiterary;
    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private Account user;
    private String isbn;
    private LocalDate yearPublishing;
    private LocalDateTime createdAt;
}
