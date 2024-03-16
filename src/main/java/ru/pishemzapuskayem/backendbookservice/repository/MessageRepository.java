package ru.pishemzapuskayem.backendbookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.UserMessage;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<UserMessage, Long> {
    List<UserMessage> findAllByUserId(Long userId);
}
