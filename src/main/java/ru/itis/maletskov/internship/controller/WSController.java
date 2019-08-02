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
import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.service.MessageParserService;
import ru.itis.maletskov.internship.service.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class WSController {
    private final MessageService messageService;
    private final ChatService chatService;
    private final MessageParserService parserService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public ServerResponseDto sendMessageToChat(@DestinationVariable Long chatId,
                                               @Payload MessageForm form) {
        form.setDateTime(LocalDateTime.now());
        form.setChatId(chatId);
        ServerResponseDto responseDto = parserService.parseMessage(form);
        messageService.saveMessage(MessageForm.fromDtoToForm(responseDto.getMessage()));
        return responseDto;
    }

    @GetMapping("/chats")
    public String chatsPage(@AuthenticationPrincipal UserAuth userAuth,
                            Model model) {
        model.addAttribute("chats", chatService.findAvailableChatsForUser(userAuth.getUsername()));
        model.addAttribute("username", userAuth.getLogin());
        model.addAttribute("formatter", formatter);
        return "chats";
    }
}
