package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.ConfirmToken;

import java.util.Optional;

@Repository
public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken, Long> {
    Optional<ConfirmToken> findConfirmTokenByToken(String token);
}
