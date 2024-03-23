package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pishemzapuskayem.backendbookservice.mapper.BookResponseMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.BookResponseDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateBookResponseRequestDTO;
import ru.pishemzapuskayem.backendbookservice.service.BookResponseService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book/response")
@RequiredArgsConstructor
public class BookResponseController {

    private final BookResponseService bookResponseService;
    private final BookResponseMapper bookResponseMapper;

    @PostMapping("create")
    public ResponseEntity<Void> createBookResponse(CreateBookResponseRequestDTO createBookResponseRequestDTO){
        bookResponseService.createBookResponse(bookResponseMapper.map(createBookResponseRequestDTO));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/book/literary/{id}")
    public ResponseEntity<List<BookResponseDTO>> bookResponseByBookLiterary(@RequestParam Long bookLiteraryId){
        return ResponseEntity.ok(
            bookResponseMapper.map(
                    bookResponseService.getListBookResponseByBookLiterary(bookLiteraryId)
            )
        );
    }

    @GetMapping("/{id}")
    public  ResponseEntity<BookResponseDTO> getBookResponseById(@RequestParam Long bookResponseId){
        return ResponseEntity.ok(
                bookResponseMapper.map(
                        bookResponseService.getBookResponseById(bookResponseId)
                )
        );
    }

}
