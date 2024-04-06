package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.UserList;

@Repository
public interface UserListRepository extends JpaRepository<UserList, Long> {
}
