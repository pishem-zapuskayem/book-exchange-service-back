package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.AuthorDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateAuthorRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.Author;

@Component
@RequiredArgsConstructor
public class AuthorMapper {
    private final ModelMapper modelMapper;

    public Author map(CreateAuthorRequestDTO dto) {
        return modelMapper.map(dto, Author.class);
    }

    public AuthorDTO map(Author author) {
        return modelMapper.map(author, AuthorDTO.class);
    }

    public Author map(AuthorDTO author) {
        return modelMapper.map(author, Author.class);
    }

    public Author map(Long authorId) {
        return (Author) new Author()
            .setId(authorId);
    }
}
