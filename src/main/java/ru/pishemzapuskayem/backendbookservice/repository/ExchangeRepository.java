package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.ExchangeList;

import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeList, Long> {
    boolean existsByFirstWishListIdAndSecondOfferListId(Long firstWishList_id, Long secondOfferList);

    List<ExchangeList> findByFirstOfferListUser(Account listOffer);

    List<ExchangeList> findBySecondOfferListUser(Account account);
}
