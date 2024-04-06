package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.dao.OfferListDAO;
import ru.pishemzapuskayem.backendbookservice.dao.WishListDAO;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.Pair;
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
import ru.pishemzapuskayem.backendbookservice.repository.UserListRepository;
import ru.pishemzapuskayem.backendbookservice.repository.WishListRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookExchangeService {
    private final WishListRepository wishListRepository;
    private final OfferListRepository offerListRepository;
    private final OfferListDAO offerListDAO;
    private final UserService userService;
    private final AddressService addressService;
    private final BookService bookService;
    private final AuthService authService;
    private final ExchangeRepository exchangeRepository;
    private final WishListDAO wishListDAO;
    private final UserListRepository userListRepository;

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

        Account account = userService.getById(wishList.getUser().getId());
        wishList.setUser(account);
        offerList.setUser(account);

        BookLiterary bookLiterary = bookService.findOrCreateByIsbn(offerList.getBookLiterary());
        offerList.setBookLiterary(bookLiterary);
        offerList.setIsbn(bookLiterary.getIsbn());
        offerList.setYearPublishing(bookLiterary.getPublishYear());
        offerList.setStatus(Status.NEW);
        offerList.setCreatedAt(LocalDateTime.now());
        offerList.getUserLists().forEach(
            ul -> ul.setOfferList(offerList)
        );

        wishList.setCreatedAt(LocalDateTime.now());
        wishList.setUpdatedAt(LocalDateTime.now());
        wishList.setStatus(Status.NEW);
        wishList.getUserLists().forEach(
            ul -> ul.setWishList(wishList)
        );

        wishListRepository.save(wishList);
        offerListRepository.save(offerList);
    }

    @Transactional
    public void createExchangeList(Pair<WishList, OfferList> firstPair, Pair<WishList, OfferList> secondPair, boolean isFullMatch) {
        wishListRepository.updateStatusByIds(
            Status.RESERVED.getId(),
            Set.of(firstPair.getFirst().getId(), secondPair.getFirst().getId())
        );

        offerListRepository.updateStatusByIds(
            Status.RESERVED.getId(),
            Set.of(firstPair.getSecond().getId(), secondPair.getSecond().getId())
        );

        ExchangeList exchangeList = new ExchangeList()
            .setFirstWishList(firstPair.getFirst())
            .setFirstOfferList(firstPair.getSecond())
            .setSecondWishList(secondPair.getFirst())
            .setSecondOfferList(secondPair.getSecond())
            .setCreatedAt(LocalDateTime.now())
            .setIsBoth(false)
            .setIsFullMatch(isFullMatch);

        exchangeRepository.save(exchangeList);
    }

    public List<WishList> findWishList(Status status){
        return wishListDAO.findWishListsByStatus(status.getId());
    }

    public WishList findWishList(Long userListId){
        UserList ul = userListRepository.findById(userListId).orElseThrow(
            () -> new ApiException("user list not found")
        );
        if (ul.getListType() == TypeList.OFFER_LIST) {
            throw new ApiException("invalid OFFER_LIST type");
        }
        return ul.getWishList();
    }

    public List<OfferList> findOfferList(Status status) {
        return offerListDAO.findOfferListsByStatus(status.getId());
    }

    public OfferList findOfferList(Long userListId) {
        UserList ul = userListRepository.findById(userListId).orElseThrow(
            () -> new ApiException("user list not found")
        );
        if (ul.getListType() == TypeList.WISH_LIST) {
            throw new ApiException("invalid WISH_LIST type");
        }
        return ul.getOfferList();
    }

    public boolean existsExchangeList(WishList wish, OfferList offer) {
        return exchangeRepository.existsByFirstWishListIdAndSecondOfferListId(wish.getId(), offer.getId());
    }
}