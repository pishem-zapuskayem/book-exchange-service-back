package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public interface OfferListRepository extends JpaRepository<OfferList, Long> {
    @EntityGraph(attributePaths = {"userLists", "userLists.categories"})
    List<OfferList> findByStatus(Status status);

    @Modifying
    @Query("UPDATE OfferList e SET e.status = :status WHERE e.id IN :ids")
    void updateStatusByIds(@Param("status") int status, @Param("ids") Set<Long> ids);

    @Query(value =
        "SELECT ol.*, ul.*, uvc.*, c.* " +
        "FROM offer_list ol " +
        "LEFT JOIN user_list ul ON ol.id = ul.offer_list_id " +
        "LEFT JOIN user_value_category uvc ON ul.id = uvc.user_list_id " +
        "LEFT JOIN category c ON uvc.category_id = c.id " +
        "WHERE ol.status = :status", nativeQuery = true
    )
    List<Object[]> findOfferListsByStatus(@Param("status") int status);
}
