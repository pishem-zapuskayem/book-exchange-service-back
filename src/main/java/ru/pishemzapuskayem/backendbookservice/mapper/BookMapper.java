package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.BookDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateBookRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final ModelMapper modelMapper;
    private final AuthorMapper authorMapper;

    public BookDTO map(BookLiterary bookLiterary) {
        BookDTO bookDTO = modelMapper.map(bookLiterary, BookDTO.class);
        bookDTO.setAuthor(
            authorMapper.map(bookLiterary.getAuthor())
        );
        return bookDTO;
    }

    public BookLiterary map(CreateBookRequestDTO dto) {
        BookLiterary book = modelMapper.map(dto, BookLiterary.class);
        book.setAuthor(
            authorMapper.map(dto.getAuthorId())
        );
        return book;
    }

    public BookLiterary map(BookDTO dto) {
        BookLiterary book = modelMapper.map(dto, BookLiterary.class);
        book.setAuthor(
            authorMapper.map(dto.getAuthor())
        );
        return book;
    }

    public BookLiterary map(Long bookLiteraryId){
        return (BookLiterary) new BookLiterary()
                .setId(bookLiteraryId);
    }

    public List<BookDTO> map(List<BookLiterary> offerListAccount) {
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (var offer: offerListAccount) {
            bookDTOS.add(map(offer));
        }
        return bookDTOS;
    }
}
