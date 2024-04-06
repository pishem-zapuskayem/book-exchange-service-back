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

    @Column(unique = true)
    private String isbn;

    private Integer publishYear;

    @Column(length = 50)
    private String note;

    @ManyToOne
    @JoinColumn(name = "idAuthor", referencedColumnName = "id")
    private Author author;

    public BookLiterary setId(Long id) {
        super.setId(id);
        return this;
    }
}
