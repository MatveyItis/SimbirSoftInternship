package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.repository.MessageRepository;
import ru.itis.maletskov.internship.service.MessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAll(new Sort(Sort.Direction.ASC, "dateTime"));
    }
}
