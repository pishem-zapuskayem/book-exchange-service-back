package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.ListType;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.repository.CategoryRepository;
import ru.pishemzapuskayem.backendbookservice.repository.WishListRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final WishListRepository wishListRepository;

    public Boolean checkFullCategory() {
        return categoryRepository.findAll().isEmpty();
    }

    @Transactional
    public Category findOrCreate(Category category) {
        Optional<Category> categoryOpt = categoryRepository.findByNameAndParent(category.getName(), category.getParent());
        return categoryOpt.orElseGet(() -> createCategory(category));
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

    public List<Category> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();
        return buildCategoryTree(allCategories);
    }

    /**
     * Ожидается что родительские категории уже подгружены или объекты присоединены к сессии
     */
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

    public List<Category> extractCategories(OfferList offerList) {
        return offerList.getUserLists().stream()
            .filter(ul -> ul.getListType() == ListType.OFFER_LIST)
            .flatMap(ul -> ul.getCategories().stream())
            .map(uc -> uc.getCategory())
            .collect(Collectors.toList());
    }

    public List<Category> extractCategories(WishList wish) {
        return wish.getUserLists().stream()
            .filter(ul -> ul.getListType() == ListType.WISH_LIST)
            .flatMap(ul -> ul.getCategories().stream())
            .map(uc -> uc.getCategory())
            .collect(Collectors.toList());
    }
}
