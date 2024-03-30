package ru.pishemzapuskayem.backendbookservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Category;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserValueCategory;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeFinderService {
    private final BookExchangeService bookExchangeService;

    // todo каждые 5 минут надо составлять карточки обмена
//    @Scheduled(fixedDelay = 300000)
//    @Async
    public void searchExchangeOffers() {
        //todo логика неверная надо целые пары сравнивать wish offer или соединять позже
        List<WishList> wishes = bookExchangeService.findWishList(Status.NEW);
        List<OfferList> offers = bookExchangeService.findOfferList(Status.NEW);
        Map<Long, Set<OfferList>> mappedCategoryOffer = mapOffersByCategories(offers);
        Map<WishList, Set<Long>> mappedWishCategories = mapWishesByCategories(wishes);
        Map<WishList, Set<OfferList>> partialMatch = new HashMap<>();
        Map<WishList, Set<OfferList>> fullMatch = new HashMap<>();
        // todo остаётся сопоставить две карты разделив совпадения на категории частичное/полное
        for (var wish: wishes) {
            Set<Long> categoryIds = mappedWishCategories.get(wish);
            // перебираем все категории из wish находим offer которые имеют эту категорию
            Set<OfferList> anyMatches = new HashSet<>();

            //как определить полное или частичное
            // получить сначала список offers которые подходят хотя бы по одному
            // далее определить оттуда по категориям
            for (var catId : categoryIds) {
                if (mappedCategoryOffer.containsKey(catId)) {
                    anyMatches.addAll(mappedCategoryOffer.get(catId));
                }
            }

            //определение категории совпадения
            // совпало но нужно знать совпало по какому id
            //
            int matches;
            for (var offer : anyMatches) {

            }
        }

        //todo второй раунд где будет проверка с той стороны подходят ли предложения или нет
        // посмотреть ещё раз по структуре определить по userId?
    }

    private Map<Long, Set<OfferList>> mapOffersByCategories(List<OfferList> offerLists) {
        Map<Long, Set<OfferList>> map = new HashMap<>();
        for (var offer : offerLists) {
            List<Category> categories = new ArrayList<>();
            for (var ul: offer.getUserLists()) {
                categories.addAll(
                    ul.getCategories()
                        .stream()
                        .map(UserValueCategory::getCategory)
                        .toList()
                );
            }

            for (var cat: categories) {
                if (map.containsKey(cat.getId())) {
                    map.get(cat.getId()).add(offer);
                } else {
                    Set<OfferList> offerSet = new HashSet<>();
                    offerSet.add(offer);
                    map.put(cat.getId(), offerSet);
                }
            }
        }
        return map;
    }

    private Map<WishList, Set<Long>> mapWishesByCategories(List<WishList> wishes) {
        Map<WishList, Set<Long>> map = new HashMap<>();
        for (var wish : wishes) {
            Set<Long> categoryIds = new HashSet<>();
            for (var ul: wish.getUserLists()) {
                categoryIds.addAll(
                    ul.getCategories()
                        .stream()
                        .map(usvlcat -> usvlcat.getCategory().getId())
                        .collect(Collectors.toSet())
                );
            }
            map.put(wish, categoryIds);
        }
        return map;
    }
}