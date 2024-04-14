package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.ExchangeList;
import ru.pishemzapuskayem.backendbookservice.repository.AccountRepository;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RatingService {
    private final AuthService authService;
    private final AccountRepository accountRepository;
    private final BookExchangeService bookExchangeService;

    @Transactional
    public void upRatingAccount(Long id) {
        Account account = authService.getAuthenticated();
        ExchangeList exchangeStatus = bookExchangeService.getExchangeCard(id);
        if (Objects.equals(account.getId(), exchangeStatus.getFirstOfferList().getUser().getId())){
           addRating(exchangeStatus.getSecondOfferList().getUser());
        }
        else {
            addRating(exchangeStatus.getFirstOfferList().getUser());
        }
    }

    public void addRating(Account account) {
        Integer rating = 1 + account.getRating() ;
        accountRepository.save(account.setRating(rating));
    }
}
