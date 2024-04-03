package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

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

    public List<Category> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();
        return buildCategoryTree(allCategories);
    }

    private List<Category> buildCategoryTree(List<Category> categories) {
        Map<Long, Category> categoryMap = new HashMap<>();
        List<Category> rootCategories = new ArrayList<>();

        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
            category.setChildren(new HashSet<>());
        }

        for (Category category : categories) {
            Category parent = category.getParent();
            if (parent != null) {
                Category parentCategory = categoryMap.get(parent.getId());
                parentCategory.getChildren().add(category);
            } else {
                rootCategories.add(category);
            }
        }

        return rootCategories;
    }
}
