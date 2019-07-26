package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.ChatType;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.service.MessageService;

@RestController
@RequiredArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
public class MessageController {
    private final MessageService messageService;
    private final ChatService chatService;

    @PostMapping(value = "/create_room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Chat> createRoom(@RequestParam("chatName") String name,
                                           @RequestParam("username") String username,
                                           @RequestParam("chatType") Boolean chatType,
                                           @AuthenticationPrincipal UserAuth userAuth) {
        if (!username.equals(userAuth.getLogin())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Chat chat = chatService.createChat(name, userAuth.getLogin(), chatType);
        return ResponseEntity.ok().body(chat);
    }

    @PostMapping(value = "/remove_room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeRoom(@RequestParam("chatName") String chatName,
                                     @AuthenticationPrincipal UserAuth userAuth) {
        Chat chat = chatService.findChatByName(chatName);
        if (chat != null) {
            chatService.deleteChat(chatName, userAuth.getUsername());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/connect_room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Chat> connectRoom(@RequestParam("chatName") String chatName,
                                            @RequestParam("username") String username,
                                            @AuthenticationPrincipal UserAuth userAuth) {
        Chat chat = chatService.findChatByName(chatName);
        if (chat != null) {
            if (chat.getType().name().equals(ChatType.PRIVATE.name())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (username.equals(userAuth.getUsername())) {
                chatService.addUserToChat(chat.getName(), username);
            }
            return ResponseEntity.ok().body(chat);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
