package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.model.Chat;

import java.util.List;

public interface ChatService {
    Chat createChat(String name, String ownerLogin, Boolean chatType);

    void addUserToChat(String chatName, String username, String otherUsername);

    void nominateToModerator(Long chatId, String userLogin);

    void downgradeToUser(Long chatId, String userLogin);

    void deleteChat(String chatName, String username);

    Chat renameChat(Chat chat, String newChatName, String username);

    List<Chat> findAllChats();

    List<Chat> findAvailableChatsForUser(String username);

    Chat findChatById(Long chatId);

    Chat findChatByName(String chatName);
}
