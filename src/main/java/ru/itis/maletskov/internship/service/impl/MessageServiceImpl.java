package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.form.UtilMessageForm;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.repository.ChatRepository;
import ru.itis.maletskov.internship.repository.MessageRepository;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.BanService;
import ru.itis.maletskov.internship.service.MessageService;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final BanService banService;

    @Override
    public MessageDto saveMessage(MessageForm form) throws InvalidAccessException {
        Optional<User> sender = userRepository.findByLogin(form.getSender());
        if (!sender.isPresent() && form.getSender() == null) {
            throw new EntityNotFoundException("Sender with name := " + form.getSender() + " is not found");
        }
        if (sender.isPresent() && banService.isUserBannedAtTheTime(sender.get().getLogin())) {
            throw new InvalidAccessException("Cannot send message. User banned at the time");
        }
        Message message = MessageForm.fromFormToMessage(form);
        message.setSender(sender.orElse(null));
        message.setChat(chatRepository.findById(form.getChatId()).orElse(null));
        return MessageDto.fromMessageToDto(messageRepository.save(message));
    }

    @Override
    public void deleteMessageById(String username, Long id) throws InvalidAccessException {
        Optional<User> user = userRepository.findByLogin(username);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("Sender with name := " + username + " is not found");
        }
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (!messageOptional.isPresent()) {
            throw new EntityNotFoundException("Message with id := " + username + " is not found");
        }
        Chat chat = messageOptional.get().getChat();
        if ((chat.getAdmin() != null && chat.getAdmin().equals(user.get())) || chat.getOwner().equals(user.get()) ||
                (chat.getModerators() != null && chat.getModerators().contains(user.get()))) {
            messageRepository.deleteById(id);
        } else {
            throw new InvalidAccessException("Insufficient rights");
        }
    }

    @Override
    public List<MessageDto> findAllMessages() {
        List<Message> messages = messageRepository.findAll(new Sort(Sort.Direction.ASC, "dateTime"));
        List<MessageDto> dtos = new ArrayList<>();
        messages.forEach(m -> dtos.add(MessageDto.fromMessageToDto(m)));
        return dtos;
    }

    @Override
    public MessageDto saveUtilMessage(ServerResponseDto responseDto, Long chatId) throws Exception {
        if (responseDto.getUtilMessage() != null) {
            UtilMessageForm form = new UtilMessageForm();
            form.setChatId(chatId);
            form.setDateTime(LocalDateTime.now());
            form.setType(responseDto.getMessage().getType());
            form.setUtilMessage(responseDto.getUtilMessage());

            Message message = new Message();
            message.setType(form.getType());
            message.setDateTime(form.getDateTime());
            Optional<Chat> chat = chatRepository.findById(form.getChatId());
            if (!chat.isPresent()) {
                throw new EntityNotFoundException("Chat with id := " + form.getChatId() + " is not found");
            }
            message.setChat(chat.get());
            message.setText(form.getUtilMessage());
            return MessageDto.fromMessageToDto(messageRepository.save(message));
        } else {
            return new MessageDto();
        }
    }
}
