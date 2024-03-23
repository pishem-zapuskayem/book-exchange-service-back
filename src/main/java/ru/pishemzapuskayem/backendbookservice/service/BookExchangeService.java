package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.AccountAddress;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;
import ru.pishemzapuskayem.backendbookservice.repository.OfferListRepository;
import ru.pishemzapuskayem.backendbookservice.repository.WishListRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookExchangeService {
    private final WishListRepository wishListRepository;
    private final OfferListRepository offerListRepository;
    private final UserService userService;
    private final AddressService addressService;

    //todo QA
    @Transactional
    public void createExchangeRequest(WishList wishList, OfferList offerList) {
        Account account = userService.getById(wishList.getUser().getId());
        wishList.setUser(account);
        offerList.setUser(account);
        wishList.setCreatedAt(LocalDateTime.now());
        wishList.setUpdatedAt(LocalDateTime.now());
        wishList.setStatus(Status.NEW);
        AccountAddress addr = addressService.createAddress(wishList.getAddress());
        wishList.setAddress(addr);
        wishListRepository.save(wishList);
        offerListRepository.save(offerList);
    }
}