package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.ChatType;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.repository.ChatRepository;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.util.comparator.MessageDateTimeComparator;
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
    public Chat createChat(String name, String ownerLogin, Boolean chatType) {
        Chat chat = new Chat();
        chat.setName(name);
        chat.setType(chatType ? ChatType.PRIVATE : ChatType.PUBLIC);
        Optional<User> candidate = userRepository.findByLogin(ownerLogin);
        if (candidate.isPresent()) {
            chat.setOwner(candidate.get());
            chat.setMembers(Collections.singleton(candidate.get()));
            if (!chatRepository.existsChatByName(name)) {
                return chatRepository.save(chat);
            } else {
                throw new ChatException(String.format(ExceptionMessages.CHAT_ALREADY_EXISTS_MESSAGE, name));
            }
        } else {
            throw new UsernameNotFoundException(String.format("Cannot create chat. User with login: %s is not found", ownerLogin));
        }
    }

    @Override
    public void addUserToChat(String chatName, String userLogin) {
        Optional<Chat> chatCandidate = chatRepository.findChatByName(chatName);
        if (chatCandidate.isPresent()) {
            Chat chat = chatCandidate.get();
            Optional<User> userCandidate = userRepository.findByLogin(userLogin);
            chat.getMembers().add(userCandidate.orElseThrow(() ->
                            new UsernameNotFoundException(String.format("Cannot add user to chat. User with login: %s is not found", userLogin))
                    )
            );
            chatRepository.save(chat);
        } else {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatName));
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
    public void deleteChat(String chatName, String username) {
        Optional<Chat> chatCandidate = chatRepository.findChatByName(chatName);
        Optional<User> userCandidate = userRepository.findByLogin(username);
        if (chatCandidate.isPresent() && userCandidate.isPresent() &&
                (chatCandidate.get().getAdmin().equals(userCandidate.get()) ||
                        chatCandidate.get().getOwner().equals(userCandidate.get()))) {
            chatRepository.delete(chatCandidate.get());
        }
    }

    @Override
    public void renameChat(String chatName, String newChatName) {
        Optional<Chat> chatCandidate = chatRepository.findChatByName(chatName);
        if (chatCandidate.isPresent()) {
            Chat chat = chatCandidate.get();
            if (chatRepository.existsChatByName(newChatName)) {
                throw new ChatException(String.format(ExceptionMessages.CHAT_ALREADY_EXISTS_MESSAGE, newChatName));
            } else {
                chat.setName(newChatName);
                chatRepository.save(chat);
            }
        }
    }

    @Override
    public List<Chat> findAllChats() {
        List<Chat> chats = chatRepository.findAll();
        chats.forEach(c -> c.getMessages().sort(new MessageDateTimeComparator()));
        return chats;
    }

    @Override
    public List<Chat> findAvailableChatsForUser(String username) {
        return chatRepository.findChatsByMembersContains(userRepository.findByLogin(username).orElse(null));
    }

    @Override
    public Chat findChatById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow(() ->
                new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId))
        );
    }

    @Override
    public Chat findChatByName(String chatName) {
        return chatRepository.findChatByName(chatName).orElse(null);
    }
}
