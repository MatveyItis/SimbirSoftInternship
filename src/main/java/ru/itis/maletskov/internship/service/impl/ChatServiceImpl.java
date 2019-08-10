package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.dto.ChatDto;
import ru.itis.maletskov.internship.model.Ban;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.model.type.ChatType;
import ru.itis.maletskov.internship.repository.BanRepository;
import ru.itis.maletskov.internship.repository.ChatRepository;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.ChatService;
import ru.itis.maletskov.internship.util.comparator.ChatIdComparator;
import ru.itis.maletskov.internship.util.exception.ChatException;
import ru.itis.maletskov.internship.util.exception.ExceptionMessages;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final BanRepository banRepository;

    @Override
    public ChatDto createChat(String name, String ownerLogin, Boolean chatType) throws ChatException {
        Chat chat = new Chat();
        chat.setName(name);
        chat.setCreatedChatDate(LocalDateTime.now());
        chat.setType(chatType ? ChatType.PRIVATE : ChatType.PUBLIC);
        Optional<User> candidate = userRepository.findByLogin(ownerLogin);
        if (!candidate.isPresent()) {
            throw new UsernameNotFoundException(String.format("Cannot create chat. User with login: %s is not found", ownerLogin));
        }
        chat.setOwner(candidate.get());
        chat.setMembers(Collections.singleton(candidate.get()));
        if (chatRepository.existsChatByName(name)) {
            throw new ChatException(String.format(ExceptionMessages.CHAT_ALREADY_EXISTS_MESSAGE, name));
        }
        return ChatDto.fromChatToDto(chatRepository.save(chat));
    }

    @Override
    public ChatDto addUserToChat(String chatName, String username, String otherUsername) throws InvalidAccessException, ChatException {
        Optional<Chat> chatCandidate = chatRepository.findChatByName(chatName);
        if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatName));
        }
        Optional<User> otherUserCandidate = userRepository.findByLogin(otherUsername);
        if (otherUsername != null && !otherUserCandidate.isPresent()) {
            throw new EntityNotFoundException("Cannot add user to chat. User with login " + otherUsername + " is not found");
        }
        Optional<User> userCandidate = userRepository.findByLogin(username);
        boolean usersPresented = userCandidate.isPresent() && otherUserCandidate.isPresent();
        if (usersPresented && (banRepository.existsBanByUserAndEndOfBannedAfter(userCandidate.get(), LocalDateTime.now()) ||
                banRepository.existsBanByUserAndEndOfBannedAfter(otherUserCandidate.get(), LocalDateTime.now()))) {
            throw new InvalidAccessException("Cannot add user to chat. User is banned");
        }
        Chat chat = chatCandidate.get();
        if (usersPresented && ((!chat.getOwner().equals(userCandidate.get())) && ((chat.getAdmin() == null) || !chat.getAdmin().equals(userCandidate.get())))
                && (chat.getType() == ChatType.PRIVATE)) {
            throw new InvalidAccessException("Cannot add user to private chat. Insufficient rights");
        }
        if (otherUserCandidate.isPresent() && chat.getMembers().contains(otherUserCandidate.get())) {
            throw new ChatException("Cannot add user to chat because user already in chat");
        }
        if (usersPresented && (chat.getOwner().equals(userCandidate.get()) || (chat.getAdmin() != null && chat.getAdmin().equals(userCandidate.get())))) {
            chat.getMembers().add(otherUserCandidate.get());
        } else {
            userCandidate.ifPresent(user -> chat.getMembers().add(user));
        }
        return ChatDto.fromChatToDto(chatRepository.save(chat));
    }

    @Override
    public ChatDto nominateToModerator(Long chatId, String userLogin, String username) throws ChatException, InvalidAccessException {
        Optional<Chat> chatCandidate = chatRepository.findById(chatId);
        if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId));
        }
        Optional<User> moderatorCandidate = userRepository.findByLogin(userLogin.trim());
        if (!moderatorCandidate.isPresent()) {
            throw new EntityNotFoundException("Cannot nominate '" + userLogin + "' to moderator. User is not found");
        }
        Optional<User> userCandidate = userRepository.findByLogin(username);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException("How do you make this ???");
        }
        Chat chat = chatCandidate.get();
        if (chat.getModerators() != null && chat.getModerators().contains(moderatorCandidate.get())) {
            throw new ChatException("Cannot nominate. '" + userLogin + "' is already a moderator");
        }
        if (chat.getMembers().contains(userCandidate.get()) && (chat.getOwner().equals(userCandidate.get()) ||
                (chat.getAdmin() != null && chat.getAdmin().equals(userCandidate.get())))) {
            chat.getMembers().add(moderatorCandidate.get());
            chat.getModerators().add(moderatorCandidate.get());
        } else {
            throw new InvalidAccessException("Insufficient rights. Cannot nominate user to moderator");
        }
        return ChatDto.fromChatToDto(chatRepository.save(chat));
    }

    @Override
    public ChatDto downgradeToUser(Long chatId, String userLogin, String username) throws ChatException, InvalidAccessException {
        Optional<Chat> chatCandidate = chatRepository.findById(chatId);
        if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId));
        }
        Optional<User> userCandidate = userRepository.findByLogin(userLogin);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException("Cannot downgrade '" + userLogin + "' to user. User is not found");
        }
        Optional<User> uCandidate = userRepository.findByLogin(username);
        if (!uCandidate.isPresent()) {
            throw new EntityNotFoundException("How do you make this ???");
        }
        Chat chat = chatCandidate.get();
        if (chat.getModerators() != null && !chat.getModerators().contains(userCandidate.get())) {
            throw new ChatException("Cannot downgrade user to user.");
        }
        if (chat.getModerators() != null && chat.getModerators().contains(userCandidate.get()) &&
                chat.getMembers().contains(uCandidate.get()) && (chat.getOwner().equals(uCandidate.get()) ||
                (chat.getAdmin() != null && chat.getAdmin().equals(uCandidate.get())))) {
            chat.getModerators().remove(userCandidate.get());
        } else {
            throw new InvalidAccessException("Insufficient rights. Cannot downgrade moderator to user");
        }
        return ChatDto.fromChatToDto(chatRepository.save(chat));
    }

    @Override
    public void deleteChat(String chatName, String username) throws InvalidAccessException {
        Optional<Chat> chatCandidate = chatRepository.findChatByName(chatName);
        if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatName));
        }
        Optional<User> userCandidate = userRepository.findByLogin(username);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException("User with name := " + username + " is not found");
        }
        if ((chatCandidate.get().getAdmin() != null && chatCandidate.get().getAdmin().equals(userCandidate.get())) ||
                chatCandidate.get().getOwner().equals(userCandidate.get())) {
            chatRepository.deleteById(chatCandidate.get().getId());
        } else {
            throw new InvalidAccessException("Insufficient rights. Cannot remove chat");
        }
    }

    @Override
    public ChatDto renameChat(Long chatId, String newChatName, String username) throws ChatException, InvalidAccessException {
        Optional<Chat> chatCandidate = chatRepository.findById(chatId);
        if (chatRepository.existsChatByName(newChatName)) {
            throw new ChatException(String.format(ExceptionMessages.CHAT_ALREADY_EXISTS_MESSAGE, newChatName));
        } else if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId));
        } else {
            Chat chat = chatCandidate.get();
            Optional<User> userCandidate = userRepository.findByLogin(username);
            if (userCandidate.isPresent() && (chat.getOwner().equals(userCandidate.get()) ||
                    (chat.getAdmin() != null && chat.getAdmin().equals(userCandidate.get())))) {
                chat.setName(newChatName);
                return ChatDto.fromChatToDto(chatRepository.save(chat));
            } else {
                throw new InvalidAccessException("Insufficient rights. Cannot rename chat");
            }
        }
    }

    @Override
    public List<ChatDto> findAvailableChatsForUser(String username) {
        List<Chat> chats = chatRepository.findChatsByMembersContains(
                userRepository.findByLogin(username).orElseThrow(() ->
                        new EntityNotFoundException("User with login := " + username + " is not found"))
        );
        chats.sort(new ChatIdComparator());
        List<ChatDto> chatsDto = new ArrayList<>();
        chats.forEach(c -> chatsDto.add(ChatDto.fromChatToDto(c)));
        return chatsDto;
    }

    @Override
    public ChatDto findChatById(Long chatId) {
        Optional<Chat> chatCandidate = chatRepository.findById(chatId);
        if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatId));
        }
        return ChatDto.fromChatToDto(chatCandidate.get());
    }

    @Override
    public ChatDto findChatByName(String chatName) {
        Optional<Chat> chatCandidate = chatRepository.findChatByName(chatName);
        if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatName));
        }
        return ChatDto.fromChatToDto(chatCandidate.get());
    }

    @Override
    public Boolean existsChatById(Long id) {
        return chatRepository.existsChatById(id);
    }

    @Override
    public ChatDto exitFromChat(String chatName, String username) throws ChatException {
        Optional<User> userCandidate = userRepository.findByLogin(username);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException("User with name := " + username + " is not found");
        }
        Optional<Chat> chatCandidate = chatRepository.findChatByName(chatName);
        if (!chatCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.CHAT_NOT_FOUND_MESSAGE, chatName));
        }
        if (!chatCandidate.get().getMembers().contains(userCandidate.get())) {
            throw new ChatException("Cannot disconnect from chat with name := " + chatName + ". You are not a chat member");
        } else {
            chatCandidate.get().getMembers().remove(userCandidate.get());
        }
        return ChatDto.fromChatToDto(chatRepository.save(chatCandidate.get()));
    }

    @Override
    public ChatDto exitFromChat(String chatName, String loginUser, Integer minute, String username) {
        return null;
    }

    @Override
    public Boolean banUser(String userLogin, Integer minuteCount, String username) {
        Optional<User> bannedCandidate = userRepository.findByLogin(userLogin);
        if (!bannedCandidate.isPresent()) {
            throw new EntityNotFoundException("User with name := " + userLogin + " is not found");
        }
        List<Ban> bans = banRepository.findBansByUser(bannedCandidate.get());
        if (isContainsActiveBan(bans)) {
            Ban activeBan = getActiveBan(bans);
            assert activeBan != null;
            activeBan.setEndOfBanned(activeBan.getEndOfBanned().plusMinutes(minuteCount));
            banRepository.save(activeBan);
        } else {
            Ban ban = new Ban();
            ban.setUser(bannedCandidate.get());
            ban.setBeginOfBanned(LocalDateTime.now());
            ban.setEndOfBanned(LocalDateTime.now().plusMinutes(minuteCount));
            banRepository.save(ban);
        }
        List<Chat> chats = chatRepository.findChatsByMembersContains(bannedCandidate.get());
        chats.forEach(c -> c.getMembers().remove(bannedCandidate.get()));
        return true;
    }

    private Boolean isContainsActiveBan(List<Ban> bans) {
        if (!bans.isEmpty()) {
            for (Ban ban : bans) {
                if (ban.getEndOfBanned().compareTo(LocalDateTime.now()) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private Ban getActiveBan(List<Ban> bans) {
        for (Ban ban : bans) {
            if (ban.getEndOfBanned().compareTo(LocalDateTime.now()) > 0) {
                return ban;
            }
        }
        return null;
    }
}
