package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.BookResponseDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateBookResponseRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookResponse;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookResponseMapper {

    private final ModelMapper modelMapper;
    private final BookMapper bookMapper;
    private final AccountMapper accountMapper;


    public BookResponseDTO map(BookResponse bookResponse) {
        BookResponseDTO bookResponseDTO = modelMapper.map(bookResponse, BookResponseDTO.class);
        bookResponseDTO.setAccount(accountMapper.map(bookResponse.getAccount()));
        bookResponseDTO.setBookLiterary(bookMapper.map(bookResponse.getBookLiterary()));
        return bookResponseDTO;
    }

    public List<BookResponseDTO> map(List<BookResponse> bookResponses) {
        List<BookResponseDTO> bookResponseDTOS = new ArrayList<>();
        for (BookResponse bookResponse : bookResponses) {
            bookResponseDTOS.add(map(bookResponse));
        }

        return bookResponseDTOS;
    }

    public BookResponse map(CreateBookResponseRequestDTO createBookResponseRequestDTO) {
        BookResponse bookResponse = modelMapper.map(createBookResponseRequestDTO, BookResponse.class);
        bookResponse.setBookLiterary(bookMapper.map(createBookResponseRequestDTO.getBookLiterary()));
        return bookResponse;
    }
}
