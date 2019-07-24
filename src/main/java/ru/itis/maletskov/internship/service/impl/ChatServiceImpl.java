package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.repository.ChatRepository;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.util.exception.ChatException;
import ru.itis.maletskov.internship.util.exception.EntityNotFoundException;
import ru.itis.maletskov.internship.util.exception.ExceptionMessages;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    public Chat createChat(String name, String ownerLogin) {
        Chat chat = new Chat();
        chat.setName(name);
        Optional<User> candidate = userRepository.findByLogin(ownerLogin);
        if (candidate.isPresent()) {
            chat.setOwner(candidate.get());
            chat.setMembers(Collections.singleton(candidate.get()));
            return chatRepository.save(chat);
        } else {
            throw new UsernameNotFoundException(String.format("Cannot create chat. User with login: %s is not found", ownerLogin));
        }
    }

    @Override
    public void addUser(Long chatId, String userLogin) {
        Optional<Chat> chatCandidate = chatRepository.findById(chatId);
        if (chatCandidate.isPresent()) {
            Chat chat = chatCandidate.get();
            Optional<User> userCandidate = userRepository.findByLogin(userLogin);
            chat.getMembers().add(userCandidate.orElseThrow(() ->
                            new UsernameNotFoundException(String.format("Cannot add user to chat. User with login: %s is not found", userLogin))
                    )
            );
            chatRepository.save(chat);
        } else {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId));
        }
    }

    @Override
    public void nominateToModerator(Long chatId, String userLogin) {
        Optional<Chat> chatCandidate = chatRepository.findById(chatId);
        if (chatCandidate.isPresent()) {
            Chat chat = chatCandidate.get();
            Optional<User> userCandidate = userRepository.findByLogin(userLogin);
            chat.getModerators().add(userCandidate.orElseThrow(() ->
                            new UsernameNotFoundException(
                                    String.format("Cannot assign user to role MODERATOR. User with login: %s is not found", userLogin)
                            )
                    )
            );
            chatRepository.save(chat);
        } else {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId));
        }
    }

    @Override
    public void downgradeToUser(Long chatId, String userLogin) {
        Optional<Chat> chatCandidate = chatRepository.findById(chatId);
        if (chatCandidate.isPresent()) {
            Chat chat = chatCandidate.get();
            Optional<User> userCandidate = userRepository.findByLogin(userLogin);
            userCandidate.ifPresent(user -> chat.getModerators().remove(user));
            chatRepository.save(chat);
        } else {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId));
        }
    }

    @Override
    public void deleteChat(String chatName) {
        Optional<Chat> chatCandidate = chatRepository.findByName(chatName);
        chatCandidate.ifPresent(chatRepository::delete);
    }

    @Override
    public void renameChat(String chatName, String newChatName) {
        Optional<Chat> chatCandidate = chatRepository.findByName(chatName);
        if (chatCandidate.isPresent()) {
            Chat chat = chatCandidate.get();
            if (chatRepository.existsChatByName(newChatName)) {
                throw new ChatException(String.format(ExceptionMessages.CHAT_CANNOT_UPDATE_MESSAGE, newChatName));
            } else {
                chat.setName(newChatName);
                chatRepository.save(chat);
            }
        }
    }

    @Override
    public List<Chat> findAllChats() {
        return chatRepository.findAll();
    }
}
