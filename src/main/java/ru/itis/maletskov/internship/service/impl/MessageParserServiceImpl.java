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
import ru.itis.maletskov.internship.util.exception.CommandParsingException;

@Service
@RequiredArgsConstructor
public class MessageParserServiceImpl implements MessageParserService {
    private final MessageService messageService;
    private final ChatService chatService;

    @Override
    public ServerResponseDto parseMessage(MessageForm form) {
        ServerResponseDto response = new ServerResponseDto();
        String command = form.getText();
        if (command.contains("//room create ")) {
            createRoom(command, response, form);
        } else if (command.contains("//room connect ")) {
            connectRoom(command, response, form);
        } else if (command.contains("//room rename ")) {
            renameRoom(command, response, form);
        } else if (command.contains("//room remove ")) {
            removeRoom(command, response, form);
        } else if (command.contains("//room disconnect ")) {
            disconnectRoom(command, response, form);
        } else if (command.contains("//user ban ")) {
            banUser(command, response, form);
        } else {
            response.setType(MessageType.MESSAGE);
            response.setMessage(MessageDto.fromFormToDto(form));
        }
        return response;
    }

    private void createRoom(String command, ServerResponseDto response, MessageForm form) {
        boolean isPrivate = command.contains(" -c ");
        String chatName = command.substring(isPrivate ? 17 : 14);
        response.setType(MessageType.COMMAND);
        chatService.createChat(chatName, form.getSender(), isPrivate);
    }

    private void connectRoom(String command, ServerResponseDto response, MessageForm form) {
        boolean containsLogin = command.contains(" -l ");
        String chatName = command.substring(15, containsLogin ? command.indexOf(" -l ") : command.length()).trim();
        String userLogin = null;
        if (containsLogin) {
            userLogin = command.substring(command.indexOf(" -l ") + 4).trim();
        }
        response.setType(MessageType.COMMAND);
        chatService.addUserToChat(chatName, form.getSender(), userLogin);
    }

    private void renameRoom(String command, ServerResponseDto response, MessageForm form) {
        String newChatName = command.substring(14);
        response.setType(MessageType.COMMAND);
        chatService.renameChat(form.getChatId(), newChatName, form.getSender());
    }

    private void removeRoom(String command, ServerResponseDto response, MessageForm form) {
        String chatName = command.substring(14);
        response.setType(MessageType.COMMAND);
        chatService.deleteChat(chatName, form.getSender());
    }

    private void disconnectRoom(String command, ServerResponseDto response, MessageForm form) {
        ChatDto chatDto = chatService.findChatById(form.getChatId());
        boolean isContainsUser = command.contains(" -l ");
        boolean isContainsMinute = command.contains(" -m ");
        boolean isContainsChatName = command.trim().length() > 17 && !isContainsUser && !isContainsMinute;
        if (isContainsMinute && isContainsUser) {
            String userLogin = command.substring(command.indexOf(" -l ") + 4, command.indexOf(" -m "));
            Integer minuteCount = Integer.valueOf(command.substring(command.indexOf(" -m ") + 4));
            chatService.exitFromChat(chatDto.getName(), userLogin, minuteCount, form.getSender());
        } else if (!isContainsChatName) {
            chatService.exitFromChat(chatDto.getName(), form.getSender());
        } else {
            String chatName = command.substring(18);
            chatService.exitFromChat(chatName, form.getSender());
        }
        response.setType(MessageType.COMMAND);
    }

    private void banUser(String command, ServerResponseDto response, MessageForm form) {
        boolean isContainsUserLogin = command.contains(" -l ");
        boolean isContainsMinuteCount = command.contains(" -m ");
        if (isContainsMinuteCount && isContainsUserLogin) {
            String userLogin = command.substring(command.indexOf(" -l ") + 4, command.indexOf(" -m "));
            Integer minuteCount = Integer.valueOf(command.substring(command.indexOf(" -m ") + 4));
            chatService.banUser(userLogin, minuteCount, form.getSender());
            response.setType(MessageType.COMMAND);
        } else {
            throw new CommandParsingException("Invalid command. Required attributes ' -l ' and ' -m ' is empty");
        }
    }
}
