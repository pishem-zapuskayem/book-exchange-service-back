package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.dao.OfferListDAO;
import ru.pishemzapuskayem.backendbookservice.dao.WishListDAO;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.ExchangeSide;
import ru.pishemzapuskayem.backendbookservice.model.Pair;
import ru.pishemzapuskayem.backendbookservice.model.entity.*;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;
import ru.pishemzapuskayem.backendbookservice.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
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
    private final UserExchangeListRepository exchangeStatusesRepository;

    @Transactional
    public void createExchangeRequest(WishList wishList, OfferList offerList) {
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

        wishList.setCreatedAt(LocalDateTime.now());
        wishList.setUpdatedAt(LocalDateTime.now());
        wishList.setStatus(Status.NEW);

        saveUserLists(wishList, offerList);

        wishListRepository.save(wishList);
        offerListRepository.save(offerList);
    }

    @Transactional
    public List<UserList> saveUserLists(WishList wishList, OfferList offerList) {
        List<UserList> userLists = new ArrayList<>();
        userLists.addAll(wishList.getUserLists());
        userLists.addAll(offerList.getUserLists());

        wishList.setUserLists(userLists);
        offerList.setUserLists(userLists);

        wishList.getUserLists().forEach(
            ul -> {
                ul.setWishList(wishList);
                ul.setOfferList(offerList);
                ul.getCategories().forEach(
                    uvc -> {
                        uvc.setUserList(ul);
                    }
                );
            }
        );

        return userListRepository.saveAll(userLists);
    }

    @Transactional
    public void createExchangeList(Pair<WishList, OfferList> firstPair, Pair<WishList, OfferList> secondPair, boolean isFullMatch) {
        wishListRepository.updateStatusByIds(
            Status.AWAITING.getId(),
            Set.of(
                firstPair.getFirst().getId(),
                secondPair.getFirst().getId()
            )
        );

        offerListRepository.updateStatusByIds(
            Status.AWAITING.getId(),
            Set.of(
                firstPair.getSecond().getId(),
                secondPair.getSecond().getId()
            )
        );

        ExchangeList exchangeList = new ExchangeList()
            .setFirstWishList(firstPair.getFirst())
            .setFirstOfferList(firstPair.getSecond())
            .setSecondWishList(secondPair.getFirst())
            .setSecondOfferList(secondPair.getSecond())
            .setCreatedAt(LocalDateTime.now())
            .setIsFirstAgreed(false)
            .setIsSecondAgreed(false)
            .setIsFullMatch(isFullMatch);

        exchangeRepository.save(exchangeList);
    }

    public List<WishList> findWishesByStatuses(Set<Status> statuses){
        return wishListDAO.findWishListsByStatus(statuses);
    }

    public WishList findWish(Long userListId){
        UserList ul = userListRepository.findById(userListId).orElseThrow(
            () -> new ApiException("user list not found")
        );
        if (ul.getListType() == ListType.OFFER_LIST) {
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
        if (ul.getListType() == ListType.WISH_LIST) {
            throw new ApiException("invalid WISH_LIST type");
        }
        return ul.getOfferList();
    }

    public boolean existsExchangeList(WishList wish, OfferList offer) {
        return exchangeRepository.existsByFirstWishListIdAndSecondOfferListId(wish.getId(), offer.getId());
    }

    public List<ExchangeList> getExchanges() {
        Account account = authService.getAuthenticated();
        return exchangeRepository.findByFirstOfferListUserOrSecondOfferListUser(account);
    }

    @Transactional
    public ExchangeList enterExchange(Long exchangeId) {
        Account user = authService.getAuthenticated();
        ExchangeList exchange = exchangeRepository.findById(exchangeId)
            .orElseThrow(() -> new ApiException("Exchange not found"));

        if (isAnyOfferHaveStatus(exchange, Status.IN_ACTIVE_EXCHANGE)) {
            throw new ApiException("Exchange is already confirmed by both sides");
        }

        ExchangeSide side = getMySide(exchange, user);
        if (side == ExchangeSide.NONE) {
            throw new ApiException("Access denied");
        }

        if (side == ExchangeSide.FIRST) {
            exchange.setIsFirstAgreed(true);
        } else {
            exchange.setIsSecondAgreed(true);
        }

        updateStatuses(exchange, Status.RESERVED);

        return exchangeRepository.save(exchange);
    }

    public boolean isAnyOfferHaveStatus(ExchangeList exchange, Status status) {
        return exchange.getFirstWishList().getStatus() == status ||
            exchange.getSecondWishList().getStatus() == status ||
            exchange.getFirstOfferList().getStatus() == status ||
            exchange.getSecondOfferList().getStatus() == status;
    }

    @Async
    @Transactional
    public void createActiveExchange(ExchangeList exchange) {
        List<UserExchangeList> exchangeStatuses = List.of(
            new UserExchangeList()
                .setExchangeList(exchange)
                .setReceiving(false)
                .setTrackNumber(null)
                .setOfferList(exchange.getFirstOfferList()),
            new UserExchangeList()
                .setExchangeList(exchange)
                .setReceiving(false)
                .setOfferList(exchange.getSecondOfferList())
        );

        exchangeStatusesRepository.saveAll(exchangeStatuses);
        updateStatuses(exchange, Status.IN_ACTIVE_EXCHANGE);

        //todo просто удалять нормально? может лучше статус обновлять на Cancelled?
        exchangeRepository.deleteExchangesWithOffersOrWishes(
            exchange.getFirstOfferList().getId(),
            exchange.getSecondOfferList().getId(),
            exchange.getFirstWishList().getId(),
            exchange.getSecondWishList().getId(),
            exchange.getId()
        );
    }

    @Transactional
    public void markAsReceived(Long exchangeId) {
        Account user = authService.getAuthenticated();
        UserExchangeList exchangeStatus = getExchangeStatus(exchangeId, user);
        exchangeStatus.setReceiving(true);
        exchangeStatusesRepository.save(exchangeStatus);
    }

    @Transactional
    public void setDeliveryTrackNumber(Long exchangeId, String deliveryTrackNumber) {
        Account user = authService.getAuthenticated();
        UserExchangeList exchangeStatus = getExchangeStatus(exchangeId, user);
        exchangeStatus.setTrackNumber(deliveryTrackNumber);
        exchangeStatusesRepository.save(exchangeStatus);
    }

    public ExchangeSide getMySide(ExchangeList exchange){
        return getMySide(exchange, authService.getAuthenticated());
    }

    public ExchangeSide getMySide(ExchangeList exchange, Account user) {
        if (user == null || exchange == null) {
            return ExchangeSide.NONE;
        }

        if (user.equals(exchange.getFirstWishList().getUser())) {
            return ExchangeSide.FIRST;
        } else if (user.equals(exchange.getSecondWishList().getUser())) {
            return ExchangeSide.SECOND;
        } else {
            return ExchangeSide.NONE;
        }
    }

    public UserExchangeList getExchangeStatus(Long exchangeId, Account user) {
        UserExchangeList exchangeStatus = null;
        List<UserExchangeList> exchangeStatuses = exchangeStatusesRepository.findAllByExchangeListId(exchangeId);
        if (!exchangeStatuses.isEmpty()) {
            exchangeStatus = exchangeStatuses.stream()
                .filter(es -> user.equals(es.getOfferList().getUser()))
                .findFirst().orElseThrow(
                    () -> new ApiException("Exchange status not found")
                );
        }
        return exchangeStatus;
    }

    @Transactional
    public void updateStatuses(ExchangeList exchange, Status status) {
        wishListRepository.updateStatusByIds(
            status.getId(),
            Set.of(
                exchange.getFirstWishList().getId(),
                exchange.getSecondWishList().getId()
            )
        );

        offerListRepository.updateStatusByIds(
            status.getId(),
            Set.of(
                exchange.getFirstOfferList().getId(),
                exchange.getSecondOfferList().getId()
            )
        );
    }

    @Async
    @Transactional
    public void tryArchive(Long exchangeId) {
        List<UserExchangeList> exchangeStatuses =
            exchangeStatusesRepository.findAllByExchangeListId(exchangeId);
        AtomicBoolean booksReceived = new AtomicBoolean(true);
        exchangeStatuses.forEach(
            status -> {
                if (!status.getReceiving()) {
                    booksReceived.set(false);
                }
            }
        );
        if (booksReceived.get()) {
          updateStatuses(exchangeStatuses.get(0).getExchangeList(), Status.CLOSED);
        }
    }

    public ExchangeList getExchangeCard(Long id){
        return exchangeRepository.findById(id).orElseThrow(
                () -> new ApiException("Такой карты нет")
        );
    }
}