package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.AccountAddressMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.WishListDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;
import ru.pishemzapuskayem.backendbookservice.service.WishListService;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wish-list")
public class WishListController {
    private final WishListService wishListService;
    private final CategoryService categoryService;
    private final AccountAddressMapper accountAddressMapper;

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<WishListDTO>> getWishList() {
        List<WishList> wishList = wishListService.getWishListsByStatuses(
            EnumSet.of(Status.AWAITING, Status.NEW, Status.CANCELLED)
        );
        List<WishListDTO> wishListDTOS = new ArrayList<>();
        for (var wish : wishList) {
            wishListDTOS.add(
                new WishListDTO(
                    wish.getId(),
                    categoryService.extractCategories(wish),
                    accountAddressMapper.map(wish.getAddress()),
                    wish.getStatus()
                )
            );
        }
        return ResponseEntity.ok(wishListDTOS);
    }
}
