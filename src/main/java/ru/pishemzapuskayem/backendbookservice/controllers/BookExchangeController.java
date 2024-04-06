package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pishemzapuskayem.backendbookservice.events.OffersUpdatedEvent;
import ru.pishemzapuskayem.backendbookservice.mapper.BookExchangeMapper;
import ru.pishemzapuskayem.backendbookservice.mapper.BookMapper;
import ru.pishemzapuskayem.backendbookservice.model.dto.CreateExchangeRequestDTO;
import ru.pishemzapuskayem.backendbookservice.model.dto.ExchangeDTO;
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
    private ResponseEntity<?> createExchangeRequest(@RequestBody CreateExchangeRequestDTO dto) {
        WishList wishList = bookExchangeMapper.mapWishList(dto);
        OfferList offerList = bookExchangeMapper.mapOfferList(dto);
        bookExchangeService.createExchangeRequest(wishList, offerList);
        eventPublisher.publishEvent(new OffersUpdatedEvent(this));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getListExchangeRequest() {
        List<ExchangeList> exchanges = bookExchangeService.getListExchange();
        ArrayList<ExchangeDTO> dtos = new ArrayList<ExchangeDTO>();
        for (ExchangeList exchangeList : exchanges) {
            dtos.add(
              new ExchangeDTO(
                  bookMapper.map(exchangeList.getFirstOfferList().getBookLiterary()),
                  bookMapper.map(exchangeList.getSecondOfferList().getBookLiterary()),
                  exchangeList.getIsFullMatch()
              )
            );
        }
        return ResponseEntity.ok(dtos);
    }
}
