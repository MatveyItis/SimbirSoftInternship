package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.service.MessageService;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final MessageService messageService;

    @GetMapping(value = "/chat_count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatCountForUser(@AuthenticationPrincipal UserAuth userAuth) {
        return ResponseEntity.ok(chatService.getChatCountsForUser(userAuth.getUsername()));
    }

    @PostMapping(value = "/delete_message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteMessage(@AuthenticationPrincipal UserAuth userAuth,
                                        @RequestParam("id") Long id) throws InvalidAccessException {
        messageService.deleteMessageById(userAuth.getUsername(), id);
        return ResponseEntity.ok(true);
    }
}
