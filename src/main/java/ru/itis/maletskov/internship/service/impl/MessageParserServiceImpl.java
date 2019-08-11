package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.dto.*;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.model.type.CommandType;
import ru.itis.maletskov.internship.model.type.MessageType;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.service.MessageParserService;
import ru.itis.maletskov.internship.service.UserService;
import ru.itis.maletskov.internship.service.YouTubeService;
import ru.itis.maletskov.internship.util.BotHelper;
import ru.itis.maletskov.internship.util.exception.ChatException;
import ru.itis.maletskov.internship.util.exception.CommandParsingException;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;
import ru.itis.maletskov.internship.util.exception.YBotException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MessageParserServiceImpl implements MessageParserService {
    private final ChatService chatService;
    private final UserService userService;
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
        } else if (command.contains("//room remove")) {
            removeRoom(command, response, form);
        } else if (command.contains("//room disconnect")) {
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
            yBotHelp(response, form);
        } else if (command.contains("//help")) {
            chatBotHelp(response, form);
        } else {
            handleMessage(response, form);
        }
        return response;
    }


    private void createRoom(String command, ServerResponseDto response, MessageForm form) throws Exception {
        boolean isPrivate = command.contains(" -c ");
        String chatName = command.substring(isPrivate ? 17 : 14);
        ChatDto chatDto = chatService.createChat(chatName, form.getSender(), isPrivate);
        response.getMessage().setType(MessageType.COMMAND);
        response.setUtilMessage("Chat with name '" + chatDto.getName() + "' has been created");

        ResponseDataDto responseData = new ResponseDataDto();
        responseData.setCommandType(CommandType.CREATE_ROOM);
        responseData.setCreatedChatName(chatDto.getName());
        responseData.setChatId(chatDto.getId());
        responseData.setUsername(chatDto.getOwner().getLogin());

        response.setResponseData(responseData);
    }

    private void connectRoom(String command, ServerResponseDto response, MessageForm form) throws Exception {
        boolean containsLogin = command.contains(" -l ");
        String chatName = command.substring(15, containsLogin ? command.indexOf(" -l ") : command.length()).trim();
        String userLogin = null;
        if (containsLogin) {
            userLogin = command.substring(command.indexOf(" -l ") + 4).trim();
        }
        response.getMessage().setType(MessageType.COMMAND);
        ChatDto chatDto = chatService.addUserToChat(chatName, form.getSender(), userLogin);
        response.setUtilMessage("Added '" + (userLogin != null ? userLogin : form.getSender()) + "' to chat.");

        ResponseDataDto responseData = new ResponseDataDto();
        responseData.setCommandType(CommandType.CONNECT_ROOM);
        responseData.setConnectedChat(ConnectedChatDto.fromDtoToConnectedChatDto(chatDto));
        responseData.setConnectedUserLogin(userLogin != null ? userLogin : form.getSender());
        response.setResponseData(responseData);
    }

    private void renameRoom(String command, ServerResponseDto response, MessageForm form) throws Exception {
        String newChatName = command.substring(14);
        ChatDto chatDto = chatService.renameChat(form.getChatId(), newChatName, form.getSender());
        response.getMessage().setType(MessageType.COMMAND);
        response.setUtilMessage("Chat has been renamed. New name is '" + chatDto.getName() + "'");

        ResponseDataDto responseData = new ResponseDataDto();
        responseData.setCommandType(CommandType.RENAME_ROOM);
        responseData.setChatId(chatDto.getId());
        responseData.setRenamedChatName(chatDto.getName());
        response.setResponseData(responseData);
    }

    private void removeRoom(String command, ServerResponseDto response, MessageForm form) throws InvalidAccessException {
        String chatName = command.substring(14);
        chatService.deleteChat(chatName, form.getSender());
        response.getMessage().setType(MessageType.COMMAND);

        ResponseDataDto responseData = new ResponseDataDto();
        responseData.setRemovedChatName(chatName);
        responseData.setChatId(form.getChatId());
        responseData.setCommandType(CommandType.REMOVE_ROOM);
        response.setResponseData(responseData);
        response.setUtilMessage("Chat has been removed:(");
    }

    private void disconnectRoom(String command, ServerResponseDto response, MessageForm form) throws ChatException {
        ChatDto chatDto = chatService.findChatById(form.getChatId());
        boolean isContainsUser = command.contains(" -l ");
        boolean isContainsMinute = command.contains(" -m ");
        boolean isContainsChatName = command.trim().length() > 17 && !isContainsUser && !isContainsMinute;
        ChatDto dto;
        ResponseDataDto responseData = new ResponseDataDto();
        if (isContainsMinute && isContainsUser) {
            String userLogin = command.substring(command.indexOf(" -l ") + 4, command.indexOf(" -m "));
            Integer minuteCount = Integer.valueOf(command.substring(command.indexOf(" -m ") + 4));
            dto = chatService.exitFromChat(chatDto.getName(), userLogin, minuteCount, form.getSender());
            response.setUtilMessage("'" + userLogin + "' has disconnected from the chat");
            responseData.setDisconnectedChatName(dto.getName());
            responseData.setDisconnectedUserLogin(userLogin);
            responseData.setDisconnectedMinuteCount(minuteCount);
        } else if (!isContainsChatName) {
            dto = chatService.exitFromChat(chatDto.getName(), form.getSender());
            response.setUtilMessage("'" + form.getSender() + "' has left the chat");
            responseData.setDisconnectedChatName(dto.getName());
            responseData.setDisconnectedUserLogin(form.getSender());
        } else {
            String chatName = command.substring(18);
            dto = chatService.exitFromChat(chatName, form.getSender());
            response.setUtilMessage("'" + form.getSender() + "' has left from the chat '" + chatName + "'");
            responseData.setDisconnectedChatName(chatName);
            responseData.setDisconnectedUserLogin(form.getSender());
        }
        response.getMessage().setType(MessageType.COMMAND);
        responseData.setChatId(dto.getId());
        responseData.setCommandType(CommandType.DISCONNECT_ROOM);
        response.setResponseData(responseData);
    }

    private void banUser(String command, ServerResponseDto response, MessageForm form) throws CommandParsingException {
        boolean isContainsUserLogin = command.contains(" -l ");
        boolean isContainsMinuteCount = command.contains(" -m ");
        if (isContainsMinuteCount && isContainsUserLogin) {
            String userLogin = command.substring(command.indexOf(" -l ") + 4, command.indexOf(" -m "));
            Integer minuteCount = Integer.valueOf(command.substring(command.indexOf(" -m ") + 4));
            chatService.banUser(userLogin, minuteCount, form.getSender());
            response.getMessage().setType(MessageType.COMMAND);
            response.setUtilMessage("User " + userLogin + " has been banned for " + minuteCount + " minutes");

            ResponseDataDto responseData = new ResponseDataDto();
            responseData.setCommandType(CommandType.USER_BAN);
            responseData.setBannedUserLogin(userLogin);
            responseData.setBannedMinuteCount(minuteCount);
            response.setResponseData(responseData);
        } else {
            throw new CommandParsingException("Invalid command. Required attributes ' -l ' and ' -m ' is empty");
        }
    }


    private void renameUser(String command, ServerResponseDto response, MessageForm form) throws InvalidAccessException {
        String newUsername = command.substring(13).trim();
        UserDto userDto = userService.renameUser(form.getSender(), newUsername);
        response.setUtilMessage("'" + form.getSender() + "' rename your username. New username is '" + newUsername + "'");
        form.setSender(userDto.getLogin());
        response.setMessage(MessageDto.fromFormToDto(form));
        response.getMessage().setType(MessageType.COMMAND);

        ResponseDataDto dataDto = new ResponseDataDto();
        dataDto.setCommandType(CommandType.USER_RENAME);
        dataDto.setRenamedUserLogin(userDto.getLogin());

        response.setResponseData(dataDto);
    }

    private void actionWithModerator(String command, ServerResponseDto response, MessageForm form) throws CommandParsingException, ChatException, InvalidAccessException {
        boolean nominated = command.contains(" -n") || command.contains(" -n ");
        boolean downgraded = command.contains(" -d") || command.contains(" -d ");
        if ((nominated && downgraded) || (!nominated && !downgraded)) {
            throw new CommandParsingException("Parse command error := '" + command + "'");
        }
        if (nominated) {
            String userLogin = command.substring(17, command.indexOf(" -n"));
            chatService.nominateToModerator(form.getChatId(), userLogin, form.getSender());
            response.getMessage().setType(MessageType.COMMAND);
            response.setUtilMessage("User with name " + userLogin + " has been nominated to moderator");
        }
        if (downgraded) {
            String userLogin = command.substring(17, command.indexOf(" -d"));
            chatService.downgradeToUser(form.getChatId(), userLogin, form.getSender());
            response.getMessage().setType(MessageType.COMMAND);
            response.setUtilMessage("User with name " + userLogin + " has been downgraded to user");
        }
    }

    private void findVideo(String command, ServerResponseDto response) throws CommandParsingException, YBotException {
        boolean isContainsChannelName = command.contains(" -k ");
        boolean isContainsVideoName = command.contains(" -w ");
        boolean isContainsViewMarker = command.contains(" -v ");
        boolean isContainsLikeMarker = command.contains(" -l ");
        if (!isContainsChannelName || !isContainsVideoName) {
            throw new CommandParsingException("Missing required attributes ' -k ' {channel name} and ' -l ' {video name}");
        }
        String channelName = command.substring(command.indexOf(" -k ") + 4, command.indexOf(" -w "));
        String videoName = command.substring(command.indexOf(" -w ") + 4);
        try {
            String utilMessage = youTubeService.searchVideo(channelName, videoName, isContainsViewMarker, isContainsLikeMarker);
            response.setUtilMessage(utilMessage);
            response.getMessage().setType(MessageType.YBOT_COMMAND);
        } catch (IOException | JSONException e) {
            throw new YBotException(e.getMessage(), e);
        }
    }

    private void yBotHelp(ServerResponseDto response, MessageForm form) {
        form.setType(MessageType.YBOT_COMMAND);
        response.setMessage(MessageDto.fromFormToDto(form));
        response.setUtilMessage(BotHelper.getYBotInfo());
    }

    private void chatBotHelp(ServerResponseDto response, MessageForm form) {
        form.setType(MessageType.COMMAND);
        response.setMessage(MessageDto.fromFormToDto(form));
        response.setUtilMessage(BotHelper.getChatBotInfo());
    }

    private void handleMessage(ServerResponseDto response, MessageForm form) {
        form.setType(MessageType.MESSAGE);
        response.setMessage(MessageDto.fromFormToDto(form));
    }
}
