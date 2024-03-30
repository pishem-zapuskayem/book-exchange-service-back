package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthService authorService;

    @Transactional
    public void update(Account account) {
        Account auth = authorService.getAuthenticated();

        if (account.getFirstName() != null) {
            auth.setFirstName(account.getFirstName());
        }

        if (account.getSecondName() != null) {
            auth.setSecondName(account.getSecondName());
        }

        if (account.getLastName() != null) {
            auth.setLastName(account.getLastName());
        }

        accountRepository.save(auth);
    }
}
