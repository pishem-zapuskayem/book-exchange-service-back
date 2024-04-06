package ru.pishemzapuskayem.backendbookservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.events.MyExchangesViewedEvent;
import ru.pishemzapuskayem.backendbookservice.events.OffersUpdatedEvent;
import ru.pishemzapuskayem.backendbookservice.events.UserLoggedInEvent;
import ru.pishemzapuskayem.backendbookservice.model.entity.AbstractEntity;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookMatchingService {
    private final BookExchangeService bookExchangeService;

    @Async
    @EventListener
    public void handleUserLoggedIn(UserLoggedInEvent event) {
        // todo точно асинхронно выполняется?
        tryFindMatchingOffers();
    }

    @Async
    @EventListener
    public void handleMyExchangesViewed(MyExchangesViewedEvent event) {
        tryFindMatchingOffers();
    }

    @Async
    @EventListener
    public void handleListUpdated(OffersUpdatedEvent event) {
        tryFindMatchingOffers();
    }

    public void tryFindMatchingOffers() {
        try {
            findMatchingOffers();
        } catch (Exception e) {
            log.error("Cant find matching offers " + e);
        }
    }

    //todo второй раунд проверка с другой стороны совпадения
    // спам запросами как то обработать
    // кешировать что можно
    // аккумуляиция запросов
    // отталкиваться от кейсов перечисленных выше (события) что конкретно подходт
    public void findMatchingOffers() {
        List<WishList> wishes = bookExchangeService.findWishList(Status.NEW);
        List<OfferList> offers = bookExchangeService.findOfferList(Status.NEW);

        if (wishes.isEmpty() || offers.isEmpty()) {
            return;
        }

        Map<Long, Set<OfferList>> mappedCategoryOffer = mapOffersByCategories(offers);
        Map<WishList, Set<Long>> mappedWishCategories = mapWishesByCategories(wishes);

        Map<WishList, Set<OfferList>> partialMatch = new HashMap<>();
        Map<WishList, Set<OfferList>> fullMatch = new HashMap<>();

        for (var wish: wishes) {
            Set<Long> wishCategoryIds = mappedWishCategories.get(wish);
            Set<OfferList> potentialMatches = new HashSet<>();

            for (var catId : wishCategoryIds) {
                if (mappedCategoryOffer.containsKey(catId)) {
                    potentialMatches.addAll(mappedCategoryOffer.get(catId).stream()
                        .filter(offer -> !offer.getUser().getId().equals(wish.getUser().getId()))
                        .collect(Collectors.toSet()));
                }
            }

            for (var offer : potentialMatches) {
                Set<Long> offerCategoryIds = offer.getUserLists().stream()
                    .flatMap(ul -> ul.getCategories().stream())
                    .map(uc -> uc.getCategory().getId())
                    .collect(Collectors.toSet());

                if (offerCategoryIds.equals(wishCategoryIds)) {
                    fullMatch.computeIfAbsent(wish, k -> new HashSet<>()).add(offer);
                } else if (!Collections.disjoint(offerCategoryIds, wishCategoryIds)) {
                    partialMatch.computeIfAbsent(wish, k -> new HashSet<>()).add(offer);
                }
            }
        }

        for (Map.Entry<WishList, Set<OfferList>> entry : fullMatch.entrySet()) {
            WishList wish = entry.getKey();
            for (OfferList offer : entry.getValue()) {
                bookExchangeService.createExchangeList(wish, offer, true);
            }
        }

        for (Map.Entry<WishList, Set<OfferList>> entry : partialMatch.entrySet()) {
            WishList wish = entry.getKey();
            for (OfferList offer : entry.getValue()) {
                bookExchangeService.createExchangeList(wish, offer, false);
            }
        }
    }

    private Map<Long, Set<OfferList>> mapOffersByCategories(List<OfferList> offerLists) {
        Map<Long, Set<OfferList>> map = new HashMap<>();
        for (OfferList offer : offerLists) {
            offer.getUserLists().stream()
                .flatMap(ul -> ul.getCategories().stream())
                .map(UserValueCategory::getCategory)
                .forEach(cat -> map.computeIfAbsent(cat.getId(), k -> new HashSet<>()).add(offer));
        }
        return map;
    }

    private Map<WishList, Set<Long>> mapWishesByCategories(List<WishList> wishes) {
        Map<WishList, Set<Long>> map = new HashMap<>();
        for (WishList wish : wishes) {
            Set<Long> categoryIds = wish.getUserLists().stream()
                .flatMap(ul -> ul.getCategories().stream())
                .map(uvc -> uvc.getCategory().getId())
                .collect(Collectors.toSet());
            map.put(wish, categoryIds);
        }
        return map;
    }
}