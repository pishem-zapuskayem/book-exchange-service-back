package ru.pishemzapuskayem.backendbookservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.security.UserDetailsImpl;

import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Account> tryGetAuthenticated() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null || principal.equals("anonymousUser")) return Optional.empty();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        Account detachedAccount = userDetails.getUser();
        return Optional.ofNullable(entityManager.find(Account.class, detachedAccount.getId()));
    }

    public Account getAuthenticated() {
        return tryGetAuthenticated().orElseThrow(
                () -> new ApiException("account not found")
        );
    }
}