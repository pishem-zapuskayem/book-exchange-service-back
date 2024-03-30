package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.CategoryMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.CategoryResponseDTO;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;


    @GetMapping("/list")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoryList() {
        return ResponseEntity.ok(
                categoryMapper.mapToList(
                        categoryService.getCategoryList()
                )
        );
    }
}
