package ru.pishemzapuskayem.backendbookservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class BookResponse extends AbstractEntity {

    @Column(length = 50)
    private String note;

    @Column(length = 500)
    private String response;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_book_literary", referencedColumnName = "id")
    private BookLiterary bookLiterary;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private Account account;
}
