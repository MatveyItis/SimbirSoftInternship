package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.dto.ChatDto;
import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.model.MessageType;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.service.MessageParserService;
import ru.itis.maletskov.internship.service.YouTubeService;
import ru.itis.maletskov.internship.util.exception.CommandParsingException;
import ru.itis.maletskov.internship.util.exception.YBotException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MessageParserServiceImpl implements MessageParserService {
    private final ChatService chatService;
    private final YouTubeService youTubeService;

    @Override
    public ServerResponseDto parseMessage(MessageForm form) throws Exception {
        ServerResponseDto response = new ServerResponseDto();
        response.setMessage(MessageDto.fromFormToDto(form));
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
        } else if (command.contains("//user rename ")) {
            renameUser(command, response, form);
        } else if (command.contains("//user moderator ")) {
            actionWithModerator(command, response, form);
        } else if (command.contains("//yBot find ")) {
            findVideo(command, response);
        } else if (command.contains("//yBot help")) {
            yBotHelp(command, response, form);
        } else if (command.contains("//help")) {
            chatBotHelp(command, response, form);
        } else {
            handleMessage(response, form);
        }
        return response;
    }


    private void createRoom(String command, ServerResponseDto response, MessageForm form) throws Exception {
        boolean isPrivate = command.contains(" -c ");
        String chatName = command.substring(isPrivate ? 17 : 14);
        response.getMessage().setType(MessageType.COMMAND);
        chatService.createChat(chatName, form.getSender(), isPrivate);
    }

    private void connectRoom(String command, ServerResponseDto response, MessageForm form) throws Exception {
        boolean containsLogin = command.contains(" -l ");
        String chatName = command.substring(15, containsLogin ? command.indexOf(" -l ") : command.length()).trim();
        String userLogin = null;
        if (containsLogin) {
            userLogin = command.substring(command.indexOf(" -l ") + 4).trim();
        }
        response.getMessage().setType(MessageType.COMMAND);
        chatService.addUserToChat(chatName, form.getSender(), userLogin);
        response.setUtilMessage("Added '" + userLogin + "' to chat.");
    }

    private void renameRoom(String command, ServerResponseDto response, MessageForm form) throws Exception {
        String newChatName = command.substring(14);
        response.getMessage().setType(MessageType.COMMAND);
        chatService.renameChat(form.getChatId(), newChatName, form.getSender());
    }

    private void removeRoom(String command, ServerResponseDto response, MessageForm form) {
        String chatName = command.substring(14);
        response.getMessage().setType(MessageType.COMMAND);
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
        response.getMessage().setType(MessageType.COMMAND);
    }

    private void banUser(String command, ServerResponseDto response, MessageForm form) throws Exception {
        boolean isContainsUserLogin = command.contains(" -l ");
        boolean isContainsMinuteCount = command.contains(" -m ");
        if (isContainsMinuteCount && isContainsUserLogin) {
            String userLogin = command.substring(command.indexOf(" -l ") + 4, command.indexOf(" -m "));
            Integer minuteCount = Integer.valueOf(command.substring(command.indexOf(" -m ") + 4));
            chatService.banUser(userLogin, minuteCount, form.getSender());
            response.getMessage().setType(MessageType.COMMAND);
        } else {
            throw new CommandParsingException("Invalid command. Required attributes ' -l ' and ' -m ' is empty");
        }
    }


    private void renameUser(String command, ServerResponseDto response, MessageForm form) {
        String newUsername = command.trim().substring(13);

    }

    private void actionWithModerator(String command, ServerResponseDto response, MessageForm form) throws Exception {
        boolean nominated = command.contains(" -n ");
        boolean downgraded = command.contains(" -d ");
        if ((nominated && downgraded) || (!nominated && !downgraded)) {
            throw new CommandParsingException("Parse command error := '" + command + "'");
        }
        if (nominated) {
            String userLogin = command.substring(17, command.indexOf(" -n "));
            chatService.nominateToModerator(form.getChatId(), userLogin, form.getSender());
            response.getMessage().setType(MessageType.COMMAND);
            response.setUtilMessage("User with name " + userLogin + " has been nominated to moderator");
        }
        if (downgraded) {
            String userLogin = command.substring(17, command.indexOf(" -m "));
            chatService.downgradeToUser(form.getChatId(), userLogin, form.getSender());
            response.getMessage().setType(MessageType.COMMAND);
            response.setUtilMessage("User with name " + userLogin + " has been downgraded to user");
        }
    }

    private void findVideo(String command, ServerResponseDto response) throws Exception {
        boolean isContainsChannelName = command.contains(" -k ");
        boolean isContainsVideoName = command.contains(" -w ");
        boolean isContainsViewMarker = command.contains(" -v ");
        boolean isContainsLikeMarker = command.contains(" -l ");
        if (isContainsChannelName || isContainsVideoName) {
            throw new CommandParsingException("Missing required attributes ' -k ' {channel name} and ' -l ' {video name}");
        }
        String videoName = command.substring(command.indexOf(" -k ") + 4, command.indexOf(" -l "));
        String channelName = command.substring(command.indexOf(" -l ") + 4);
        try {
            youTubeService.searchVideo(channelName, videoName, isContainsViewMarker, isContainsLikeMarker);
            response.getMessage().setType(MessageType.YBOT_COMMAND);
        } catch (IOException | JSONException e) {
            throw new YBotException(e.getMessage());
        }
    }

    private void yBotHelp(String command, ServerResponseDto response, MessageForm form) {

    }

    private void chatBotHelp(String command, ServerResponseDto response, MessageForm form) {

    }

    private void handleMessage(ServerResponseDto response, MessageForm form) {
        form.setType(MessageType.MESSAGE);
        response.setMessage(MessageDto.fromFormToDto(form));
    }
}
