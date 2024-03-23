package ru.pishemzapuskayem.backendbookservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pishemzapuskayem.backendbookservice.exception.ApiException;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.MessageType;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.UserMessage;
import ru.pishemzapuskayem.backendbookservice.repository.MessageRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final AuthService authService;
    private final UserService userService;
    private final MessageRepository messageRepository;

    public List<UserMessage> findMessagesByUserId(Long userId) {
        if (userId == null) {
            throw new ApiException("user not found");
        }
        return messageRepository.findAllByUserId(userId);
    }

    @Transactional
    public UserMessage saveMessage(UserMessage message) {
        message.setType(
            authService.getAuthenticated().isAdmin()
                ? MessageType.OUTGOING
                : MessageType.INCOMING
        );
        message.setUser(
            userService.getById(message.getId())
        );
        return messageRepository.save(message);
    }
}
