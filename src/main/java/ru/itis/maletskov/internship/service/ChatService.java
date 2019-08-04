package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.ChatDto;

import java.util.List;

public interface ChatService {
    ChatDto createChat(String name, String ownerLogin, Boolean chatType) throws Exception;

    ChatDto addUserToChat(String chatName, String username, String otherUsername) throws Exception;

    ChatDto nominateToModerator(Long chatId, String userLogin, String username) throws Exception;

    ChatDto downgradeToUser(Long chatId, String userLogin, String username) throws Exception;

    void deleteChat(String chatName, String username);

    ChatDto renameChat(Long chatId, String newChatName, String username) throws Exception;

    List<ChatDto> findAvailableChatsForUser(String username);

    ChatDto findChatById(Long chatId);

    ChatDto findChatByName(String chatName);

    Boolean existsChatById(Long id);

    ChatDto exitFromChat(String chatName, String username) throws Exception;

    ChatDto exitFromChat(String chatName, String loginUser, Integer minute, String username) throws Exception;

    ChatDto banUser(String userLogin, Integer minuteCount, String sender) throws Exception;
}
