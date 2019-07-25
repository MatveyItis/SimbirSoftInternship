package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.model.Chat;

import java.util.List;

public interface ChatService {
    Chat createChat(String name, String ownerLogin, Boolean chatType);

    void addUser(Long chatId, String userLogin);

    void nominateToModerator(Long chatId, String userLogin);

    void downgradeToUser(Long chatId, String userLogin);

    void deleteChat(String chatName);

    void renameChat(String chatName, String newChatName);

    List<Chat> findAllChats();

    Chat findChatById(Long chatId);
}
