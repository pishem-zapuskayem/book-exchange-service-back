package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Author;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;
import ru.pishemzapuskayem.backendbookservice.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    public Author getById(Long id) {
        return authorRepository.findById(id)
            .orElseThrow(() -> new ApiException("author not found"));
    }

    @Transactional
    public Author findOrCreateAuthor(Author author) {
        Author oldAuthor = authorRepository.findByLastnameAndFirstname(author.getLastname(), author.getFirstname());
        if (oldAuthor != null) {
            return oldAuthor;
        }
        return createAuthor(author);
    }
}
