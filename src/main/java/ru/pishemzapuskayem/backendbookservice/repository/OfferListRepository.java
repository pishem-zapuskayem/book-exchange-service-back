package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.OfferList;

@Repository
public interface OfferListRepository extends JpaRepository<OfferList, Long> {
}
