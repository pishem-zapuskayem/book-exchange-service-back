package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserExchangeList;

import java.util.List;

@Repository
public interface UserExchangeListRepository extends JpaRepository<UserExchangeList, Long> {
    List<UserExchangeList> findAllByExchangeListId(Long exchangeListId);
}
