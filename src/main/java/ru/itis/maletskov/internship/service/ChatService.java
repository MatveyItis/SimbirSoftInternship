package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.ChatDto;

import java.util.List;

public interface ChatService {
    ChatDto createChat(String name, String ownerLogin, Boolean chatType);

    ChatDto addUserToChat(String chatName, String username, String otherUsername);

    void nominateToModerator(Long chatId, String userLogin, String username);

    void downgradeToUser(Long chatId, String userLogin, String username);

    void deleteChat(String chatName, String username);

    ChatDto renameChat(Long chatId, String newChatName, String username);

    List<ChatDto> findAllChats();

    List<ChatDto> findAvailableChatsForUser(String username);

    ChatDto findChatById(Long chatId);

    ChatDto findChatByName(String chatName);

    Boolean existsChatById(Long id);

    ChatDto exitFromChat(String chatName, String username);

    ChatDto exitFromChat(String chatName, String loginUser, Integer minute, String username);

    ChatDto banUser(String userLogin, Integer minuteCount, String sender);
}
