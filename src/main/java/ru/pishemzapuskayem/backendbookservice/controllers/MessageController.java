package ru.pishemzapuskayem.backendbookservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pishemzapuskayem.backendbookservice.model.entity.message.UserMessage;
import ru.pishemzapuskayem.backendbookservice.service.MessageService;

import java.util.List;

@Controller
@MessageMapping("/chat")
@RequestMapping("/api/support")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/connect")
    @SendToUser("/chat")
    public List<UserMessage> connectToChat(@Payload Long userId) {
        return messageService.findMessagesByUserId(userId);
    }


    @MessageMapping("/send")
    public void sendMessage(@Payload UserMessage message) {
        UserMessage saved = messageService.saveMessage(message);
        messagingTemplate.convertAndSendToUser(
            saved.getUser().getId().toString(),
            "/chat", saved
        );
    }
}

//    @MessageMapping("/connect")
//    public ResponseEntity<List<UserMessage>> connectToChat(@RequestMapping Long userId) {
//        return messageService.findMessagesByUserId(userId);
//    }
