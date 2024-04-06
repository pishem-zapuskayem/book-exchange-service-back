package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.BookLiterary;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.repository.WishListRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishListService {
    private WishListRepository wishListRepository;
    private AuthService authService;

    public List<WishList> getWishList() {
        Account account = authService.getAuthenticated();
        List<WishList> wishList = wishListRepository.findByUser(account);
        return wishList;
    }
}
