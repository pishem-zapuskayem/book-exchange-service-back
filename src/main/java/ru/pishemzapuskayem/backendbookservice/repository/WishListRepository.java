package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.Account;
import ru.pishemzapuskayem.backendbookservice.model.entity.WishList;

import java.util.List;
import java.util.Set;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    @Modifying
    @Query(value = "UPDATE wish_list SET status = :status WHERE id IN(:ids)", nativeQuery = true)
    void updateStatusByIds(@Param("status") Integer status, @Param("ids") Set<Long> ids);

    List<WishList> findByUser(Account account);

    @Query("SELECT w FROM WishList w WHERE w.status IN (:statuses) AND w.user = :user")
    List<WishList> findByStatusesAndUser(@Param("statuses") Set<Integer> statuses, @Param("user") Account user);
}
