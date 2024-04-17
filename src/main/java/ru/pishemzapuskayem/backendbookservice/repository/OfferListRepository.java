package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;

import java.util.List;
import java.util.Set;

@Repository
public interface OfferListRepository extends JpaRepository<OfferList, Long> {
    @Modifying
    @Query(value = "UPDATE offer_list SET status = :status WHERE id IN :ids", nativeQuery = true)
    void updateStatusByIds(@Param("status") Integer status, @Param("ids") Set<Long> ids);

    List<OfferList> findByUser(Account account);

    @Query(value = "SELECT o FROM OfferList o WHERE o.status IN (:statuses) AND o.user = :user")
    List<OfferList> findByStatusesAndUser(@Param("statuses") Set<Integer> statuses, @Param("user") Account user);
}
