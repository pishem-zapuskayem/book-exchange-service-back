package ru.pishemzapuskayem.backendbookservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.events.MyExchangesViewedEvent;
import ru.pishemzapuskayem.backendbookservice.events.OffersUpdatedEvent;
import ru.pishemzapuskayem.backendbookservice.events.UserLoggedInEvent;
import ru.pishemzapuskayem.backendbookservice.model.Pair;
import ru.pishemzapuskayem.backendbookservice.model.entity.AbstractEntity;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.TypeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = false)
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

    // todo спам запросами как то обработать
    // кешировать что можно
    // аккумуляиция запросов
    // отталкиваться от кейсов перечисленных выше (события) что конкретно подходт
    @Transactional
    public void findMatchingOffers() {
        List<Pair<WishList, OfferList>> allPairs = extractExchangePairs();
        if (allPairs.isEmpty() || allPairs.size() == 1) {
            return;
        }
        Set<String> checkedPairKeys = new HashSet<>();
        //todo оптимизация категоризации
        for (Pair<WishList, OfferList> pair : allPairs) {
            WishList wish = pair.getFirst();
            OfferList offer = pair.getSecond();
            Set<Long> wishCategoryIds = getCategoryIdsFor(wish.getUserLists(), TypeList.WISH_LIST);
            Set<Long> offerCategoryIds = getCategoryIdsFor(offer.getUserLists(), TypeList.OFFER_LIST);

            //todo на будущее делать через мапы
            for (Pair<WishList, OfferList> otherPair : allPairs) {
                if (pair.equals(otherPair)) continue;
                String pairKey = new Pair<>(pair, otherPair).generatePairKey();
                if (checkedPairKeys.contains(pairKey)) continue;

                WishList otherWish = otherPair.getFirst();
                OfferList otherOffer = otherPair.getSecond();
                Set<Long> otherWishCategoryIds = getCategoryIdsFor(otherWish.getUserLists(), TypeList.WISH_LIST);
                Set<Long> otherOfferCategoryIds = getCategoryIdsFor(otherOffer.getUserLists(), TypeList.OFFER_LIST);

                boolean fullMatch = wishCategoryIds.equals(otherOfferCategoryIds) &&
                    otherWishCategoryIds.equals(offerCategoryIds);
                boolean partialMatch = !Collections.disjoint(wishCategoryIds, otherOfferCategoryIds) &&
                    !Collections.disjoint(otherWishCategoryIds, otherOfferCategoryIds);

                if (fullMatch || partialMatch) {
                    bookExchangeService.createExchangeList(pair, otherPair, fullMatch);
                    checkedPairKeys.add(pairKey);
                }
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

    public List<Pair<WishList, OfferList>> extractExchangePairs() {
        List<Pair<WishList, OfferList>> exchangePairs = new ArrayList<>();
        List<WishList> wishes = bookExchangeService.findWishList(Status.NEW);

        //todo надо было в дао сразу подгружать
        for (WishList wish : wishes) {
            wish.getUserLists().stream()
                .filter(ul -> ul.getListType() == TypeList.OFFER_LIST)
                .findFirst()
                .map(ul -> bookExchangeService.findOfferList(ul.getId()))
                .ifPresent(relatedOffer -> exchangePairs.add(new Pair<>(wish, relatedOffer)));
        }

        return exchangePairs;
    }

    public Set<Long> getCategoryIdsFor(List<UserList> userLists, TypeList listType) {
        return userLists.stream()
            .filter(ul -> ul.getListType() == listType)
            .flatMap(ul -> ul.getCategories().stream())
            .map(uc -> uc.getCategory().getId())
            .collect(Collectors.toSet());
    }
}