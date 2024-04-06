package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.mapper.BookMapper;
import ru.pishemzapuskayem.backendbookservice.mapper.OfferListMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.BookDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.OfferDTO;
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
    private final OfferListMapper offerListMapper;
    private final CategoryService categoryService;
    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<?> getOfferList(){
        List<OfferList> offerList = offerListService.getOfferListAccount();
        List<OfferListDTO> offerListDTOS =  new ArrayList<>();
        for (var offer : offerList){
            offerListDTOS.add(
                    new OfferListDTO(
                            bookMapper.map(offer.getBookLiterary()),
                            categoryService.extractTree(offer)
                    )
            );
        }
        return ResponseEntity.ok().build();
    }
}
