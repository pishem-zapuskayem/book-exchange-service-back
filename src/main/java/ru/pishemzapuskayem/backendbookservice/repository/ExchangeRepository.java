package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.ExchangeList;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeList, Long> {
    boolean existsByFirstWishListIdAndSecondOfferListId(Long firstWishList_id, Long secondOfferList);

    @Query("SELECT e FROM ExchangeList e WHERE e.firstOfferList.user = :user OR e.secondOfferList.user = :user")
    List<ExchangeList> findByFirstOfferListUserOrSecondOfferListUser(Account user);

    @Modifying
    @Query("DELETE FROM ExchangeList e " +
        "WHERE (e.firstOfferList.id = :firstOfferId " +
        "OR e.secondOfferList.id = :secondOfferId " +
        "OR e.firstWishList.id = :firstWishId " +
        "OR e.secondWishList.id = :secondWishId) " +
        "AND e.id != :excludeExchangeId")
    void deleteExchangesWithOffersOrWishes(
        @Param("firstOfferId") Long firstOfferId,
        @Param("secondOfferId") Long secondOfferId,
        @Param("firstWishId") Long firstWishId,
        @Param("secondWishId") Long secondWishId,
        @Param("excludeExchangeId") Long excludeExchangeId
    );

    @Query("SELECT e FROM ExchangeList e " +
        "WHERE (e.firstOfferList.id = :firstOfferId " +
        "OR e.secondOfferList.id = :secondOfferId " +
        "OR e.firstWishList.id = :firstWishId " +
        "OR e.secondWishList.id = :secondWishId) " +
        "AND e.id != :excludeExchangeId")
    List<ExchangeList> findExchangesWithOffersOrWishes(
        @Param("firstOfferId") Long firstOfferId,
        @Param("secondOfferId") Long secondOfferId,
        @Param("firstWishId") Long firstWishId,
        @Param("secondWishId") Long secondWishId,
        @Param("excludeExchangeId") Long excludeExchangeId
    );

    Optional<ExchangeList> findByFirstOfferListAndSecondOfferList(OfferList first, OfferList second);

    @Query("SELECT e FROM ExchangeList e " +
        "WHERE (e.firstOfferList.status IN (:statuses) AND e.secondOfferList.status IN (:statuses)) AND " +
        "(e.firstOfferList.user = :user OR e.secondOfferList.user = :user)")
    List<ExchangeList> findAllByStatusesAndUser(@Param("user") Account user, @Param("statuses") Set<Integer> statuses);
}
