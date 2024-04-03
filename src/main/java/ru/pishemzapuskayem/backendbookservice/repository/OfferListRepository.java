package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.List;

@Repository
public interface OfferListRepository extends JpaRepository<OfferList, Long> {
    @EntityGraph(attributePaths = {"userLists", "userLists.categories"})
    List<OfferList> findByStatus(Status status);
}
