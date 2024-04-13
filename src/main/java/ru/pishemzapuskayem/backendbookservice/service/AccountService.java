package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthService authService;

    @Transactional
    public void update(Account account) {
        Account auth = authService.getAuthenticated();

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

    @Transactional
    public void lockAccountById(Long accoutnId) {
        Account account = accountRepository.findById(accoutnId)
            .orElseThrow(() -> new ApiException("Account not found"));
        account.setNonLocked(false);
        accountRepository.save(account);
    }

    public Account getAuthenticatedUserData() {
        Account account = authService.getAuthenticated();
        Hibernate.initialize(account.getAccountAddress());
        return account;
    }

    public Account getById(Long userId) {
        return accountRepository.findById(userId).orElseThrow(
            () -> new ApiException("account not found")
        );
    }
}
