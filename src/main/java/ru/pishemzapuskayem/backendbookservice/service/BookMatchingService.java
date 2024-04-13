package ru.pishemzapuskayem.backendbookservice.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.events.MyExchangesViewedEvent;
import ru.pishemzapuskayem.backendbookservice.events.OffersUpdatedEvent;
import ru.pishemzapuskayem.backendbookservice.events.UserLoggedInEvent;
import ru.pishemzapuskayem.backendbookservice.model.Pair;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.ListType;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = false)
public class BookMatchingService {

    private static final long FETCH_RATE_IN_MILLIS = 60000;

    private final BookExchangeService bookExchangeService;
    private final RateLimiter rateLimiter;

    public BookMatchingService(BookExchangeService bookExchangeService) {
        this.bookExchangeService = bookExchangeService;
        this.rateLimiter = new RateLimiter(FETCH_RATE_IN_MILLIS);
    }

    @Async
    @EventListener
    public void handleUserLoggedIn(UserLoggedInEvent event) {
        rateLimiter.tryInvoke(this::tryFindMatchingOffers, false);
    }

    @Async
    @EventListener
    public void handleMyExchangesViewed(MyExchangesViewedEvent event) {
        rateLimiter.tryInvoke(this::tryFindMatchingOffers, false);
    }

    @Async
    @EventListener
    public void handleListUpdated(OffersUpdatedEvent event) {
        rateLimiter.tryInvoke(this::tryFindMatchingOffers, true);
    }

    public void tryFindMatchingOffers() {
        try {
            findMatchingOffers();
        } catch (Exception e) {
            log.error("Cant find matching offers " + e);
        }
    }

    @Transactional
    public void findMatchingOffers() {
        List<Pair<WishList, OfferList>> allPairs = extractExchangePairs();
        if (allPairs.isEmpty() || allPairs.size() == 1) {
            return;
        }
        Set<String> createdExchangePairs = new HashSet<>();
        for (Pair<WishList, OfferList> pair : allPairs) {
            WishList wish = pair.getFirst();
            OfferList offer = pair.getSecond();
            Set<Long> wishCategoryIds = getCategoryIdsFor(wish.getUserLists(), ListType.WISH_LIST);
            Set<Long> offerCategoryIds = getCategoryIdsFor(offer.getUserLists(), ListType.OFFER_LIST);

            for (Pair<WishList, OfferList> otherPair : allPairs) {
                if (pair.equals(otherPair)) continue;
                if (isFromSameUser(pair, otherPair)) continue;
                String pairKey = Pair.generatePairKey(new Pair<>(pair, otherPair));
                if (createdExchangePairs.contains(pairKey)) continue;

                WishList otherWish = otherPair.getFirst();
                OfferList otherOffer = otherPair.getSecond();
                Set<Long> otherWishCategoryIds = getCategoryIdsFor(otherWish.getUserLists(), ListType.WISH_LIST);
                Set<Long> otherOfferCategoryIds = getCategoryIdsFor(otherOffer.getUserLists(), ListType.OFFER_LIST);

                boolean fullMatch = wishCategoryIds.equals(otherOfferCategoryIds) &&
                    otherWishCategoryIds.equals(offerCategoryIds);
                boolean partialMatch = !Collections.disjoint(wishCategoryIds, otherOfferCategoryIds) &&
                    !Collections.disjoint(otherWishCategoryIds, otherOfferCategoryIds);

                if (fullMatch || partialMatch) {
                    bookExchangeService.createExchangeList(pair, otherPair, fullMatch);
                    createdExchangePairs.add(pairKey);
                }
            }
        }
    }

    private boolean isFromSameUser(Pair<WishList, OfferList> pair, Pair<WishList, OfferList> otherPair) {
        return Objects.equals(pair.getFirst().getUser(), otherPair.getFirst().getUser());
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
        List<WishList> wishes = bookExchangeService.findWishesByStatuses(
            EnumSet.of(Status.NEW, Status.AWAITING)
        );

        //todo в дао же сразу подгружаются или здесь ещё запросы отправляются?
        for (WishList wish : wishes) {
            wish.getUserLists().stream()
                .filter(ul -> ul.getListType() == ListType.OFFER_LIST)
                .findFirst()
                .map(ul -> bookExchangeService.findOfferList(ul.getId()))
                .ifPresent(relatedOffer -> exchangePairs.add(new Pair<>(wish, relatedOffer)));
        }

        return exchangePairs;
    }

    public Set<Long> getCategoryIdsFor(List<UserList> userLists, ListType listType) {
        return userLists.stream()
            .filter(ul -> ul.getListType() == listType)
            .flatMap(ul -> ul.getCategories().stream())
            .map(uc -> uc.getCategory().getId())
            .collect(Collectors.toSet());
    }
}