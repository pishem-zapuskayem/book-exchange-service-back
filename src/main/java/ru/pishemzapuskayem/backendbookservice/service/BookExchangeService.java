package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;
import ru.pishemzapuskayem.backendbookservice.model.entity.ExchangeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.TypeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;
import ru.pishemzapuskayem.backendbookservice.repository.ExchangeRepository;
import ru.pishemzapuskayem.backendbookservice.repository.OfferListRepository;
import ru.pishemzapuskayem.backendbookservice.repository.WishListRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookExchangeService {
    private final WishListRepository wishListRepository;
    private final OfferListRepository offerListRepository;
    private final UserService userService;
    private final AddressService addressService;
    private final BookService bookService;
    private final AuthService authService;
    private final ExchangeRepository exchangeRepository
        ;

    @Transactional
    public void createExchangeRequest(WishList wishList, OfferList offerList) {

        List<UserList> userLists = new ArrayList<>();
        userLists.addAll(wishList.getUserLists());
        userLists.addAll(offerList.getUserLists());
        wishList.setUserLists(userLists);
        offerList.setUserLists(userLists);

        Account user = authService.getAuthenticated();
        wishList.setUser(user);
        offerList.setUser(user);

        wishList.getAddress().setAccount(user);
        AccountAddress addr = addressService.findOrCreateByIndex(wishList.getAddress());
        wishList.setAddress(addr);

        BookLiterary bookLiterary = bookService.findOrCreateByIsbn(offerList.getBookLiterary());
        offerList.setBookLiterary(bookLiterary);
        Account account = userService.getById(wishList.getUser().getId());
        wishList.setUser(account);
        offerList.setUser(account);
        wishList.setCreatedAt(LocalDateTime.now());
        wishList.setUpdatedAt(LocalDateTime.now());
        wishList.setStatus(Status.NEW);

        wishListRepository.save(wishList);
        offerListRepository.save(offerList);
    }

    @Transactional
    public void createExchangeList(WishList wish, OfferList offer, boolean isFullMatch) {
        if (existsExchangeList(wish, offer)) {
            return;
        }

        OfferList offerFromWish = wish.getUserLists().stream()
            .filter(ul -> ul.getListType() == TypeList.OFFER_LIST)
            .findFirst()
            .map(UserList::getOfferList)
            .orElse(null);

        WishList wishFromOffer = offer.getUserLists().stream()
            .filter(ul -> ul.getListType() == TypeList.WISH_LIST)
            .findFirst()
            .map(UserList::getWishList)
            .orElse(null);

        wishListRepository.updateStatusByIds(
            Status.RESERVED.getId(),
            Set.of(wish.getId(), wishFromOffer.getId())
        );

        offerListRepository.updateStatusByIds(
            Status.RESERVED.getId(),
            Set.of(offer.getId(), offerFromWish.getId())
        );

        ExchangeList exchangeList = new ExchangeList()
            .setFirstWishList(wish)
            .setFirstOfferList(offerFromWish)
            .setSecondOfferList(offer)
            .setSecondWishList(wishFromOffer)
            .setCreatedAt(LocalDateTime.now())
            .setIsBoth(false)
            .setIsFullMatch(isFullMatch);

        exchangeRepository.save(exchangeList);
    }

    public List<WishList> findWishList(Status status){
        return wishListRepository.findByStatus(status);
    }

    public List<OfferList> findOfferList(Status status) {
        return offerListRepository.findByStatus(status);
    }

    public boolean existsExchangeList(WishList wish, OfferList offer) {
        return exchangeRepository.existsByFirstWishListIdAndSecondOfferListId(wish.getId(), offer.getId());
    }
}