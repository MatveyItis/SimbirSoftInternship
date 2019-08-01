package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.repository.ChatRepository;
import ru.itis.maletskov.internship.repository.MessageRepository;
import ru.itis.maletskov.internship.service.MessageService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Override
    public MessageDto saveMessage(MessageForm messageForm) {
        Message message = new Message();
        message.setChat(chatRepository.findById(messageForm.getChatId()).orElse(null));
        message.setSender(messageForm.getSender());
        message.setDateTime(messageForm.getDateTime());
        message.setText(messageForm.getText());
        return MessageDto.fromMessageToDto(messageRepository.save(message));
    }

    @Override
    public void deleteMessageById(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public List<MessageDto> findAllMessages() {
        List<Message> messages = messageRepository.findAll(new Sort(Sort.Direction.ASC, "dateTime"));
        List<MessageDto> dtos = new ArrayList<>();
        messages.forEach(m -> dtos.add(MessageDto.fromMessageToDto(m)));
        return dtos;
    }
}
