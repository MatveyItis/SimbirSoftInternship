package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.ChatDto;
import ru.itis.maletskov.internship.util.exception.ChatException;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

import java.util.List;

public interface ChatService {
    ChatDto createChat(String name, String ownerLogin, Boolean chatType) throws ChatException;

    ChatDto addUserToChat(String chatName, String username, String otherUsername) throws InvalidAccessException, ChatException;

    ChatDto nominateToModerator(Long chatId, String userLogin, String username) throws ChatException, InvalidAccessException;

    ChatDto downgradeToUser(Long chatId, String userLogin, String username) throws ChatException, InvalidAccessException;

    void deleteChat(String chatName, String username) throws InvalidAccessException;

    ChatDto renameChat(Long chatId, String newChatName, String username) throws ChatException, InvalidAccessException;

    List<ChatDto> findAvailableChatsForUser(String username);

    ChatDto findChatById(Long chatId);

    ChatDto findChatByName(String chatName);

    Boolean existsChatById(Long id);

    ChatDto exitFromChat(String chatName, String username) throws ChatException, InvalidAccessException;

    ChatDto exitFromChat(String chatName, String loginUser, Integer minute, String username) throws ChatException;

    Boolean banUser(String userLogin, Integer minuteCount, String sender);

    Long getChatCountsForUser(String username);
}
