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
import ru.itis.maletskov.internship.dto.ChatDto;
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
    public ResponseEntity<ChatDto> createRoom(@RequestParam("chatName") String name,
                                              @RequestParam("chatType") Boolean chatType,
                                              @AuthenticationPrincipal UserAuth userAuth) {
        ChatDto chat = chatService.createChat(name, userAuth.getLogin(), chatType);
        return ResponseEntity.ok().body(chat);
    }

    @PostMapping(value = "/remove_room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeRoom(@RequestParam("chatName") String chatName,
                                     @AuthenticationPrincipal UserAuth userAuth) {
        ChatDto chat = chatService.findChatByName(chatName);
        if (chat != null) {
            chatService.deleteChat(chatName, userAuth.getUsername());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/connect_room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatDto> connectRoom(@RequestParam("chatName") String chatName,
                                               @RequestParam(value = "userLogin", required = false) String userLogin,
                                               @AuthenticationPrincipal UserAuth userAuth) {
        ChatDto chat = chatService.findChatByName(chatName);
        if (chat != null) {
            if (chat.getType().name().equals(ChatType.PRIVATE.name())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok().body(chatService.addUserToChat(chat.getName(), userAuth.getUsername(), userLogin));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/rename_room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatDto> renameRoom(@RequestParam("newChatName") String newChatName,
                                              @RequestParam("chatId") Long chatId,
                                              @AuthenticationPrincipal UserAuth userAuth) {
        if (chatService.existsChatById(chatId)) {
            return ResponseEntity.ok(chatService.renameChat(chatId, newChatName, userAuth.getUsername()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/disconnect_room", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity disconnectRoom(@RequestParam(value = "chatName", required = false) String chatName,
                                         @RequestParam("chatId") Long chatId,
                                         @RequestParam(value = "userLogin", required = false) String userLogin,
                                         @RequestParam(value = "minuteCount", required = false) String minute,
                                         @AuthenticationPrincipal UserAuth userAuth) {
        return ResponseEntity.ok().build();
    }
}
