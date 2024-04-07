package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.AccountAddressMapper;
import ru.pishemzapuskayem.backendbookservice.mapper.BookMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.WishListDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;
import ru.pishemzapuskayem.backendbookservice.service.WishListService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wish-list")
public class WishListController {
    private final WishListService wishListService;
    private final CategoryService categoryService;
    private final AccountAddressMapper accountAddressMapper;

    @GetMapping
    public ResponseEntity<?> getWishList() {
        List<WishList> wishList = wishListService.getWishList();
        List<WishListDTO> wishListDTOS = new ArrayList<>();
        for (var wish: wishList) {
            wishListDTOS.add(
                new WishListDTO(
                        categoryService.extractTree(wish),
                        accountAddressMapper.map(wish.getAddress())
                )
            );
        }

        return ResponseEntity.ok(
                wishListDTOS
        );
    }
}
