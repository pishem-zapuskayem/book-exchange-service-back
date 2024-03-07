package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.ConfirmToken;
import ru.pishemzapuskayem.backendbookservice.repository.ConfirmTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class TokenService {

    private final ConfirmTokenRepository confirmTokenRepository;

    @Value("${confirm-token.expired-after}")
    private int expiredAfter;

    @Transactional
    public ConfirmToken createToken(Account account) {
        return confirmTokenRepository.save(
                new ConfirmToken()
                .setUser(account)
                .setToken(UUID.randomUUID().toString())
                .setIsConfirmed(false)
                .setExpireAt(LocalDateTime.now().plusMinutes(expiredAfter))
        );
    }

    public Optional<ConfirmToken> find(String token) {
        return confirmTokenRepository.findConfirmTokenByToken(token);
    }

    @Transactional
    public void confirm(ConfirmToken confirmToken) {
        confirmToken.setIsConfirmed(true);
        if (confirmToken.getId() == null) throw new IllegalStateException();
        confirmTokenRepository.save(confirmToken);
    }
}
