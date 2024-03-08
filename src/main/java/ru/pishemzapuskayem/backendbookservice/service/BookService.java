package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Author;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;
import ru.pishemzapuskayem.backendbookservice.repository.BookRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookLiterary getById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ApiException("book not found"));
    }

    @Transactional
    public void create(BookLiterary book) {
        Author author = authorService.getById(book.getAuthor().getId());
        book.setAuthor(author);
        bookRepository.save(book);
    }

    public List<BookLiterary> getBooks() {
        return bookRepository.findAll();
    }
}
