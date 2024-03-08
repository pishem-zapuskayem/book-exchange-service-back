package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookResponse;
import ru.pishemzapuskayem.backendbookservice.repository.BookResponseRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookResponseService {
    private final BookResponseRepository bookResponseRepository;
    private final AuthService authService;
    private final BookService bookService;

    @Transactional
    public void createBookResponse(BookResponse bookResponse){
        bookResponse.setCreatedAt(LocalDateTime.now());
        bookResponse.setAccount(authService.getAuthenticated().get());
        bookResponse.setBookLiterary(bookService.getById(bookResponse.getBookLiterary().getId()));
        bookResponseRepository.save(bookResponse);
    }

    public List<BookResponse> getListBookResponseByBookLiterary(Long bookLiteraryId){
        return bookResponseRepository.findByBookLiterary(bookService.getById(bookLiteraryId));
    }

    public BookResponse getBookResponseById(Long bookResponseId){
        return bookResponseRepository.findById(bookResponseId)
                .orElseThrow(() -> new ApiException("Нет такого комментария"));
    }
}
