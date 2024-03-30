package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.repository.CategoryRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategoryList(){
        return categoryRepository.findAll();
    }

    @Transactional
    public Category createCategory(Category category) {
        if (category.getParent() != null) {
            Category parent = getById(category.getParent().getId());
            category.setParent(parent);
        }
        return categoryRepository.save(category);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new ApiException("Родительская категория не найдена"));
    }
}
