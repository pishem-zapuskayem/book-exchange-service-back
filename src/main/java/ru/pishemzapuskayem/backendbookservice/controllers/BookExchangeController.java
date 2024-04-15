package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;
import ru.pishemzapuskayem.backendbookservice.service.BookExchangeService;
import ru.pishemzapuskayem.backendbookservice.service.CategoryService;
import ru.pishemzapuskayem.backendbookservice.service.RatingService;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchanges")
public class BookExchangeController {
    private final BookExchangeService bookExchangeService;
    private final BookExchangeMapper exchangesMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final BookMapper bookMapper;
    private final CategoryService categoryService;
    private final RatingService ratingService;

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Void> createExchangeRequest(@RequestBody CreateExchangeRequestDTO dto) {
        WishList wishList = exchangesMapper.mapWishList(dto);
        OfferList offerList = exchangesMapper.mapOfferList(dto);
        bookExchangeService.createExchangeRequest(wishList, offerList);
        eventPublisher.publishEvent(new OffersUpdatedEvent(this));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<ExchangeDTO>> getExchanges() {
        eventPublisher.publishEvent(new MyExchangesViewedEvent(this));
        List<ExchangeList> exchanges = bookExchangeService.getMyExchangesByStatuses(
            EnumSet.of(Status.NEW, Status.AWAITING, Status.RESERVED)
        );
        return ResponseEntity.ok(exchangesMapper.map(exchanges));
    }

    @GetMapping("/list/card")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<ExchangeCardDTO> getExchangeCard(@RequestParam Long id) {
        ExchangeList exchange = bookExchangeService.getExchangeCard(id);
        ExchangeSide mySide = bookExchangeService.getMySide(exchange);
        ExchangeSides sides = processSides(exchange, mySide);
        List<Category> categories = categoryService.extractCategories(sides.getOtherOffer());
        return ResponseEntity.ok(
                new ExchangeCardDTO(
                    exchange.getId(),
                    new MyOfferDTO(
                            bookMapper.map(sides.getMyOffer().getBookLiterary()),
                            sides.getIsIAgreed()),
                        new OtherOfferDTO(
                                categories,
                                sides.getIsOtherAgreed()
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
        ratingService.upRatingAccount(exchangeId);
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

    @GetMapping("/active")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<ExchangeDTO>> getActiveExchanges() {
        List<ExchangeList> exchanges = bookExchangeService.getMyExchangesByStatuses(
            Set.of(Status.IN_ACTIVE_EXCHANGE)
        );
        return ResponseEntity.ok(exchangesMapper.map(exchanges));
    }

    @GetMapping("/active/card")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<ActiveExchangeCardDTO> getActiveExchangeCard(@RequestParam Long id) {
        ExchangeList exchange = bookExchangeService.getExchangeCard(id);
        ExchangeSide mySide = bookExchangeService.getMySide(exchange);
        ExchangeSides sides = processSides(exchange, mySide);
        List<UserExchangeList> statuses = bookExchangeService.getExchangeStatuses(id);
        if (statuses.isEmpty()) {
            throw new ApiException("error_no_statuses");
        }
        UserExchangeList firstStatus = statuses.get(0);
        UserExchangeList secondStatus = statuses.get(1);
        UserExchangeList myStatus = null;
        UserExchangeList otherStatus = null;;
        myStatus = Objects.equals(firstStatus.getOfferList(), sides.getMyOffer())
            ? firstStatus : secondStatus;
        otherStatus = Objects.equals(secondStatus.getOfferList(), sides.getOtherOffer())
            ? secondStatus : firstStatus;

        List<Category> categories = categoryService.extractCategories(sides.getOtherOffer());
        return ResponseEntity.ok(
            new ActiveExchangeCardDTO(
                exchange.getId(),
                new MyOfferDTO(
                    bookMapper.map(sides.getMyOffer().getBookLiterary()),
                    sides.getIsIAgreed()),
                new OtherOfferDTO(
                    categories,
                    sides.getIsOtherAgreed()
                ),
                new UserExchangeStatusDTO(
                    myStatus.getId(),
                    myStatus.getTrackNumber(),
                    myStatus.getReceiving()
                ),
                new UserExchangeStatusDTO(
                    otherStatus.getId(),
                    otherStatus.getTrackNumber(),
                    otherStatus.getReceiving()
                )
            )
        );
    }

    @GetMapping("/archive")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<ExchangeDTO>> getArchiveList(){
        List<ExchangeList> exchanges = bookExchangeService.getMyExchangesByStatuses(
                Set.of(Status.CLOSED)
        );
        return ResponseEntity.ok(exchangesMapper.map(exchanges));
    }


    private ExchangeSides processSides(ExchangeList exchange, ExchangeSide mySide) {
        OfferList myOffer;
        OfferList otherOffer;
        Boolean isIAgreed;
        Boolean isOtherAgreed;

        switch (mySide) {
            case FIRST:
                myOffer = exchange.getFirstOfferList();
                otherOffer = exchange.getSecondOfferList();
                isIAgreed = exchange.getIsFirstAgreed();
                isOtherAgreed = exchange.getIsSecondAgreed();
                break;
            case SECOND:
                myOffer = exchange.getSecondOfferList();
                otherOffer = exchange.getFirstOfferList();
                isIAgreed = exchange.getIsSecondAgreed();
                isOtherAgreed = exchange.getIsFirstAgreed();
                break;
            default:
                throw new ApiException("Произошла ошибка");
        }

        return new ExchangeSides(myOffer, otherOffer, isIAgreed, isOtherAgreed);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class ExchangeSides {
        private OfferList myOffer;
        private OfferList otherOffer;
        private Boolean isIAgreed;
        private Boolean isOtherAgreed;
    }
}
