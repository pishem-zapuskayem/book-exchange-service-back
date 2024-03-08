package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookLiterary extends AbstractEntity {
    @Column(name = "booke_name", length = 50)
    private String bookName;

    @Column(length = 50)
    private String note;

    @ManyToOne
    @JoinColumn(name = "idAuthor", referencedColumnName = "id")
    private Author author;
}
