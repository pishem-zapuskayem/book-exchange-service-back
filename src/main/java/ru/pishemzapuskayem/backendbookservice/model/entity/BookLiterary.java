package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class BookLiterary extends AbstractEntity {
    @Column(name = "booke_name", length = 50)
    private String bookName;

    @Column(length = 50)
    private String note;

    @ManyToOne
    private Author author;
}
