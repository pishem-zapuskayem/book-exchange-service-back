package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;
import ru.pishemzapuskayem.backendbookservice.repository.WishListRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final AuthService authService;

    public List<WishList> getWishList() {
        Account account = authService.getAuthenticated();
        List<WishList> wishList = wishListRepository.findByUser(account);
        return wishList;
    }

    public List<WishList> getWishListsByStatuses(Set<Status> statuses) {
        Account account = authService.getAuthenticated();
        return wishListRepository.findByStatusesAndUser(
            statuses.stream().map(Status::getId).collect(Collectors.toSet()), account
        );
    }
}
