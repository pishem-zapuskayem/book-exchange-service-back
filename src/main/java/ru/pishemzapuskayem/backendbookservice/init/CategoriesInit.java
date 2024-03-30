package ru.pishemzapuskayem.backendbookservice.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoriesInit implements CommandLineRunner {

    private final CategoryService service;

    @Override
    public void run(String... args) {
        Category genre = new Category()
            .setName("Жанр")
            .setMultiselect(true);
        genre = service.createCategory(genre);
        List<Category> categories = List.of(
            new Category()
                .setName("Фэнтези")
                .setParent(genre),
            new Category()
                .setName("Научная фантастика")
                .setParent(genre),
            new Category()
                .setName("Ужасы")
                .setParent(genre)
        );
        for (var category : categories) {
            service.createCategory(category);
        }
        log.info("Категории добавлены");
    }
}
