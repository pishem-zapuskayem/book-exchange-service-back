package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.Status;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    @EntityGraph(attributePaths = {"userLists", "userLists.categories"})
    List<WishList> findByStatus(Status status);
}
