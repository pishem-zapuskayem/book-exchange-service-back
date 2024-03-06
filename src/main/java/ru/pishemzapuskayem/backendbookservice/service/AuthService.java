package ru.pishemzapuskayem.backendbookservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.security.UserDetailsImpl;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Account> getAuthenticated() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account detachedAccount = userDetails.getUser();
        return Optional.ofNullable(entityManager.find(Account.class, detachedAccount.getId()));
    }
}
