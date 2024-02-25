package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.ConfirmToken;

@Repository
public interface TokenConfirmRepository extends JpaRepository<ConfirmToken, Long> {
}
