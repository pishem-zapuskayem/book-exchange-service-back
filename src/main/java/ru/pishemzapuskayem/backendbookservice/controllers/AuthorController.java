package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.AuthorMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.AuthorDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateAuthorRequestDTO;
import ru.pishemzapuskayem.backendbookservice.service.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAuthors() {
        return ResponseEntity.ok(
            authorService.getAuthors()
                .stream()
                .map(authorMapper::map)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestParam Long id) {
        return ResponseEntity.ok(authorMapper.map(authorService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody CreateAuthorRequestDTO request) {
        authorService.create(authorMapper.map(request));
        return ResponseEntity.ok().build();
    }
}
