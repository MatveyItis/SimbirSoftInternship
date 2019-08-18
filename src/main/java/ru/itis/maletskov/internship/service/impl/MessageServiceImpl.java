package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.itis.maletskov.internship.util.exception.ExceptionMessages;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final BanService banService;

    @Override
    public MessageDto saveMessage(MessageForm form) throws InvalidAccessException {
        Optional<User> sender = userRepository.findByLogin(form.getSender());
        if (!sender.isPresent() && form.getSender() == null) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND_MESSAGE, form.getSender()));
        }
        if (sender.isPresent() && banService.isUserBannedAtTheTime(sender.get().getLogin())) {
            throw new InvalidAccessException(String.format(ExceptionMessages.USER_IS_BANNED_MESSAGE, sender.get().getLogin()));
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
            throw new EntityNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND_MESSAGE, username));
        }
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (!messageOptional.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.MESSAGE_NOT_FOUND, id));
        }
        Chat chat = messageOptional.get().getChat();
        if ((chat.getAdmin() != null && chat.getAdmin().equals(user.get())) || chat.getOwner().equals(user.get()) ||
                (chat.getModerators() != null && chat.getModerators().contains(user.get()))) {
            messageRepository.deleteById(id);
        } else {
            throw new InvalidAccessException(ExceptionMessages.INSUFFICIENT_RIGHTS_MESSAGE);
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
            message.setChat(chat.orElse(null));
            message.setText(form.getUtilMessage());
            return MessageDto.fromMessageToDto(messageRepository.save(message));
        } else {
            return new MessageDto();
        }
    }
}
