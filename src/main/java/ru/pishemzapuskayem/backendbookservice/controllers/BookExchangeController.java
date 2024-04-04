package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pishemzapuskayem.backendbookservice.events.OffersUpdatedEvent;
import ru.pishemzapuskayem.backendbookservice.mapper.BookExchangeMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateExchangeRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.service.BookExchangeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchanges")
public class BookExchangeController {
    private final BookExchangeService bookExchangeService;
    private final BookExchangeMapper bookExchangeMapper;
    private final ApplicationEventPublisher eventPublisher;

    @PostMapping
    private ResponseEntity<?> createExchangeRequest(@RequestBody CreateExchangeRequestDTO dto) {
        WishList wishList = bookExchangeMapper.mapWishList(dto);
        OfferList offerList = bookExchangeMapper.mapOfferList(dto);
        bookExchangeService.createExchangeRequest(wishList, offerList);
        eventPublisher.publishEvent(new OffersUpdatedEvent(this));
        return ResponseEntity.ok().build();
    }
}
