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
import java.util.Set;

@Repository
public interface OfferListRepository extends JpaRepository<OfferList, Long> {
    @EntityGraph(attributePaths = {"userLists", "userLists.categories"})
    List<OfferList> findByStatus(Status status);

    @Modifying
    @Query("UPDATE OfferList e SET e.status = :status WHERE e.id IN :ids")
    void updateStatusByIds(@Param("status") int status, @Param("ids") Set<Long> ids);
}
