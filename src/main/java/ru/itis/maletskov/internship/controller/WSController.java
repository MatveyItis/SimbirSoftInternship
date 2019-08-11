package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.dto.UserDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.form.UtilMessageForm;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class WSController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChatService chatService;
    private final MessageParserService parserService;
    private final BanService banService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public ServerResponseDto sendMessageToChat(@DestinationVariable Long chatId,
                                               @Payload MessageForm form) throws Exception {
        form.setDateTime(LocalDateTime.now());
        form.setChatId(chatId);
        form.setText(form.getText().trim());
        ServerResponseDto responseDto = parserService.parseMessage(form);
        messageService.saveMessage(MessageForm.fromDtoToForm(responseDto.getMessage()));

        if (responseDto.getUtilMessage() != null) {
            UtilMessageForm utilForm = new UtilMessageForm();
            utilForm.setChatId(chatId);
            utilForm.setDateTime(LocalDateTime.now());
            utilForm.setType(responseDto.getMessage().getType());
            utilForm.setUtilMessage(responseDto.getUtilMessage());
            messageService.saveUtilMessage(utilForm);
        }
        return responseDto;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ServerResponseDto sendFirstMessage(@Payload MessageForm form) throws Exception {
        form.setDateTime(LocalDateTime.now());
        return parserService.parseMessage(form);
    }

    @GetMapping("/chats")
    public String chatsPage(@AuthenticationPrincipal UserAuth userAuth,
                            Model model) {
        UserDto userDto = userService.getUserById(userAuth.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuth userDetails = (UserAuth) authentication.getPrincipal();
        userDetails.setLogin(userDto.getLogin());
        Boolean banned = banService.isUserBannedAtTheTime(userAuth.getUsername());
        model.addAttribute("banned", banned);
        model.addAttribute("chats", chatService.findAvailableChatsForUser(userAuth.getUsername()));
        model.addAttribute("username", userAuth.getLogin());
        model.addAttribute("formatter", formatter);
        return "chats";
    }
}
