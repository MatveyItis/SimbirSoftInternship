package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.service.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final MessageService messageService;
    private final ChatService chatService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message sendMessage(@Payload Message message) {
        message.setDateTime(LocalDateTime.now());
        messageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/chat/{id}")
    @SendTo("/topic/messages/{id}")
    public Message sendMessageToChat(@DestinationVariable Long id,
                                     @Payload Message message) {
        message.setDateTime(LocalDateTime.now());
        messageService.saveMessage(message);
        return message;
    }

    @GetMapping("/chat")
    public String chatPage(@AuthenticationPrincipal UserAuth userAuth,
                           Model model) {
        model.addAttribute("username", userAuth.getLogin());
        model.addAttribute("messages", messageService.findAllMessages());
        model.addAttribute("formatter", formatter);
        return "chat";
    }

    @GetMapping("/chats")
    public String chatsPage(@AuthenticationPrincipal UserAuth userAuth,
                            Model model) {
        model.addAttribute("chats", chatService.findAllChats());
        model.addAttribute("username", userAuth.getLogin());
        return "chats";
    }
}
