package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.BookMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.BookDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateBookRequestDTO;
import ru.pishemzapuskayem.backendbookservice.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks() {
        return ResponseEntity.ok(
            bookService.getBooks()
                .stream()
                .map(bookMapper::map)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@RequestParam Long id) {
        return ResponseEntity.ok(bookMapper.map(bookService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<Void> createBook(@RequestBody CreateBookRequestDTO request) {
        bookService.createBook(bookMapper.map(request));
        return ResponseEntity.ok().build();
    }
}
