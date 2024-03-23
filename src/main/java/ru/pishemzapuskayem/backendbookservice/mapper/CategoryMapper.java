package ru.pishemzapuskayem.backendbookservice.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.pishemzapuskayem.backendbookservice.model.dto.CategoryDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.TypeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    private final ModelMapper modelMapper;

    public UserList map(List<CategoryDTO> wishCategories, TypeList typeList) {
        UserList listOfCategories = new UserList().setListType(typeList);
        List<Category> categories = wishCategories.stream().map(this::mapRecursive).toList();
        List<UserValueCategory> userValueCategories = categories.stream().map(
            category -> new UserValueCategory()
                .setCategory(category)
                .setUserList(listOfCategories)
        ).toList();
        return listOfCategories;
    }

    public Category mapRecursive(CategoryDTO dto) {
        Category category = map(dto);
        CategoryDTO leafDTO = dto.getParent();
        Category leaf = category;
        while (leafDTO.getParent() != null) {
            leaf.setParent(map(leafDTO.getParent()));
            leafDTO = leafDTO.getParent();
            leaf = leaf.getParent();
        }
        return category;
    }

    public Category map(CategoryDTO dto) {
        return modelMapper.map(dto, Category.class);
    }
 }
