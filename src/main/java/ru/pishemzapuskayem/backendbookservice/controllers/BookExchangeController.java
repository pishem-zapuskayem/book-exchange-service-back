package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.pishemzapuskayem.backendbookservice.events.MyExchangesViewedEvent;
import ru.pishemzapuskayem.backendbookservice.events.OffersUpdatedEvent;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.mapper.BookExchangeMapper;
import ru.pishemzapuskayem.backendbookservice.mapper.BookMapper;
import ru.pishemzapuskayem.backendbookservice.model.ExchangeSide;
import ru.pishemzapuskayem.backendbookservice.model.dto.*;
import ru.pishemzapuskayem.backendbookservice.model.entity.*;
import ru.pishemzapuskayem.backendbookservice.service.BookExchangeService;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchanges")
public class BookExchangeController {
    private final BookExchangeService bookExchangeService;
    private final BookExchangeMapper bookExchangeMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final BookMapper bookMapper;
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createExchangeRequest(@RequestBody CreateExchangeRequestDTO dto) {
        WishList wishList = bookExchangeMapper.mapWishList(dto);
        OfferList offerList = bookExchangeMapper.mapOfferList(dto);
        bookExchangeService.createExchangeRequest(wishList, offerList);
        eventPublisher.publishEvent(new OffersUpdatedEvent(this));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getListExchangeRequest() {
        eventPublisher.publishEvent(new MyExchangesViewedEvent(this));
        List<ExchangeList> exchanges = bookExchangeService.getExchanges();
        ArrayList<ExchangeDTO> dtos = new ArrayList<>();
        for (ExchangeList exchangeList : exchanges) {
            dtos.add(
              new ExchangeDTO(
                  exchangeList.getId(),
                  bookMapper.map(exchangeList.getFirstOfferList().getBookLiterary()),
                  bookMapper.map(exchangeList.getSecondOfferList().getBookLiterary()),
                  exchangeList.getIsFullMatch()
              )
            );
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/list/card")
    public ResponseEntity<?> getListExchangeCard(@RequestParam Long id) {
        ExchangeList exchangeList = bookExchangeService.getExchangeCard(id);
        ExchangeSide exchangeSide = bookExchangeService.getMySide(exchangeList);
        OfferList meOfferList = null;
        OfferList takeOfferList = null;
        Boolean meIsAgreed = null;
        Boolean takeIsAgreed = null;
        switch (exchangeSide) {
            case FIRST:
                meOfferList = exchangeList.getFirstOfferList();
                takeOfferList = exchangeList.getSecondOfferList();
                meIsAgreed = exchangeList.getIsFirstAgreed();
                takeIsAgreed = exchangeList.getIsSecondAgreed();
                break;
            case SECOND:
                meOfferList = exchangeList.getSecondOfferList();
                takeOfferList = exchangeList.getFirstOfferList();
                meIsAgreed = exchangeList.getIsSecondAgreed();
                takeIsAgreed = exchangeList.getIsFirstAgreed();
                break;
            default:
                throw new ApiException("Произошла ошибка");
        }
        List<Category> categories = categoryService.extractTree(takeOfferList);
        return ResponseEntity.ok(
                new ExchangeCardDTO(
                    new ExchangeGiftDTO(
                            bookMapper.map(meOfferList.getBookLiterary()),
                            meIsAgreed),
                        new ExchangeTakeDTO(
                                categories,
                                takeIsAgreed
                        )
                )
        );
    }

    @PostMapping("/enter")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Void> enterExchange(@RequestParam Long exchangeId) {
        ExchangeList exchange = bookExchangeService.enterExchange(exchangeId);
        if (exchange.isBothAgreed()) {
            bookExchangeService.createActiveExchange(exchange);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-as-received")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void markReceived(@RequestParam Long exchangeId) {
        bookExchangeService.markAsReceived(exchangeId);
        bookExchangeService.tryArchive(exchangeId);
    }

    @PostMapping("/set-delivery-number")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void markReceived(
            @RequestParam Long exchangeId,
            @RequestBody UpdateExchangeDeliveryRequestDTO deliveryRequestDTO
    ) {
        bookExchangeService.setDeliveryTrackNumber(exchangeId, deliveryRequestDTO.getTrackNumber());
    }
}
