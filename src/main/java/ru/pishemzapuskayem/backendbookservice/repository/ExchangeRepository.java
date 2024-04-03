package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.ExchangeList;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeList, Long> {
    boolean existsByFirstWishListIdAndSecondOfferList(Long firstWishListId, Long secondOfferListId);
}
