package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.pishemzapuskayem.backendbookservice.events.MyExchangesViewedEvent;
import ru.pishemzapuskayem.backendbookservice.events.OffersUpdatedEvent;
import ru.pishemzapuskayem.backendbookservice.mapper.BookExchangeMapper;
import ru.pishemzapuskayem.backendbookservice.mapper.BookMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateExchangeRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.ExchangeDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.UpdateExchangeDeliveryRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.entity.ExchangeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.service.BookExchangeService;

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
