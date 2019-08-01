package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.dto.ChatDto;
import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.model.MessageType;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.service.MessageParserService;
import ru.itis.maletskov.internship.service.MessageService;

@Service
@RequiredArgsConstructor
public class MessageParserServiceImpl implements MessageParserService {
    private final MessageService messageService;
    private final ChatService chatService;

    @Override
    public ServerResponseDto parseMessage(MessageForm form) {
        ServerResponseDto dto = new ServerResponseDto();
        String command = form.getText();
        if (command.contains("//room create ")) {
            boolean isPrivate = command.contains(" -c ");
            String chatName = command.substring(isPrivate ? 17 : 14);
            dto.setType(MessageType.COMMAND);
            ChatDto chat = chatService.createChat(chatName, form.getSender(), isPrivate);
        } else if (command.contains("//room connect ")) {
            boolean containsLogin = command.contains(" -l ");
            String chatName = command.substring(15, containsLogin ? command.indexOf(" -l ") : command.length());
            String userLogin = null;
            if (containsLogin) {
                userLogin = command.substring(command.indexOf(" -l ") + 4);
            }
            dto.setType(MessageType.COMMAND);
            ChatDto chatDto = chatService.addUserToChat(chatName, form.getSender(), userLogin);
        } else if (command.contains("//room rename ")) {
            String newChatName = command.substring(14);
            dto.setType(MessageType.COMMAND);
            ChatDto chatDto = chatService.renameChat(form.getChatId(), newChatName, form.getSender());
        } else if (command.contains("//room remove ")) {
            String chatName = command.substring(14);
            dto.setType(MessageType.COMMAND);
            chatService.deleteChat(chatName, form.getSender());
        } else {
            dto.setType(MessageType.MESSAGE);
            dto.setMessage(MessageDto.fromFormToDto(form));
        }
        return dto;
    }
}
