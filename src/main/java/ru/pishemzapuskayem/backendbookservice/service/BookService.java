package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Author;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;
import ru.pishemzapuskayem.backendbookservice.repository.BookRepository;

import java.util.List;
import java.util.Optional;

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
    public BookLiterary createBook(BookLiterary book) {
        Author author = authorService.findOrCreateAuthor(book.getAuthor());
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public List<BookLiterary> getBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public BookLiterary findOrCreateByIsbn(BookLiterary bookLiterary) {
        if (StringUtils.isEmpty(bookLiterary.getIsbn())){
            throw new ApiException("isbn_is_empty");
        }
        Optional<BookLiterary> book = bookRepository.findByIsbn(bookLiterary.getIsbn());
        return book.orElseGet(
            () -> createBook(bookLiterary)
        );
    }
}
