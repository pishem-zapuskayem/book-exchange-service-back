package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.CategoryDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.CategoryResponseDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.ChildCategoryResponseDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.ListType;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper modelMapper;

    public UserList mapToList(Set<Long> wishCategoriesIds, ListType listType) {
        UserList listOfCategories = new UserList().setListType(listType);
        List<Category> categories = map(wishCategoriesIds);
        List<UserValueCategory> userValueCategories = categories.stream().map(
                category -> new UserValueCategory()
                        .setCategory(category)
        ).toList();
        listOfCategories.setCategories(userValueCategories);
        return listOfCategories;
    }

    public Category mapRecursive(CategoryDTO dto) {
        Category category = mapToList(dto);
        CategoryDTO leafDTO = dto.getParent();
        Category leaf = category;
        while (leafDTO.getParent() != null) {
            leaf.setParent(mapToList(leafDTO.getParent()));
            leafDTO = leafDTO.getParent();
            leaf = leaf.getParent();
        }
        return category;
    }

    public Category mapToList(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    }

    public List<CategoryResponseDTO> mapToList(List<Category> categoryList) {
        Map<Long, CategoryResponseDTO> map = new HashMap<>();

        for (var category : categoryList) {
            if (category.getParent() == null) {
                map.put(
                        category.getId(),
                        new CategoryResponseDTO(
                                category.getId(),
                                category.getName(),
                                new ArrayList<>(),
                                category.getMultiselect()
                        )
                );
            }
            if (category.getParent() != null) {
                map.get(category.getParent().getId()).getChildren().add(
                        new ChildCategoryResponseDTO(
                                category.getId(),
                                category.getName()
                        )
                );
            }
        }

        return map.values().stream().toList();
    }

    public List<Category> map(Set<Long> ids) {
        return ids.stream().map(id -> {
            Category cat = new Category();
            cat.setId(id);
            return cat;
        }).collect(Collectors.toList());
    }
}