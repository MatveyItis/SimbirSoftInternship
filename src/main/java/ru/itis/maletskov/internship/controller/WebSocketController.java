package ru.itis.maletskov.internship.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class WebSocketController {
    private List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) throws Exception {
        message.setDateTime(LocalDateTime.now());
        messages.add(message);
        Thread.sleep(100);
        return message;
    }

    /*
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message sendMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/javainuse")
    public Message newUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }*/

    @GetMapping("/chat")
    public String chatPage(@AuthenticationPrincipal User user,
                           Model model) {
        model.addAttribute("username", user.getLogin());
        model.addAttribute("messages", messages);
        return "chat";
    }
}