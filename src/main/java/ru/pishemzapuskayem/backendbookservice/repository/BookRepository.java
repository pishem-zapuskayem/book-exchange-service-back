package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookLiterary, Long> {
    Optional<BookLiterary> findByIsbn(String isbn);
}
