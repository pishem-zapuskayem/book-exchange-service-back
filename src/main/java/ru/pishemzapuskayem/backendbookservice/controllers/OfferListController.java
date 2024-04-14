package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.BookMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.OfferListDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;
import ru.pishemzapuskayem.backendbookservice.service.OfferListService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offer-list")
public class OfferListController {

    private final OfferListService offerListService;
    private final CategoryService categoryService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<OfferListDTO>> getOfferList(){
        List<OfferList> offerList = offerListService.getOfferListAccount();
        List<OfferListDTO> offerListDTOS = new ArrayList<>();
        for (var offer : offerList) {
            offerListDTOS.add(
                new OfferListDTO(
                    offer.getId(),
                    bookMapper.map(offer.getBookLiterary()),
                    categoryService.extractCategories(offer)
                )
            );
        }
        return ResponseEntity.ok(offerListDTOS);
    }
}
