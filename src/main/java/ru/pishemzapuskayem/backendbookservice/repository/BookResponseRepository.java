package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookResponse;

import java.util.List;


@Repository
public interface BookResponseRepository extends JpaRepository<BookResponse, Long> {
    List<BookResponse> findByBookLiterary(BookLiterary byId);
}
