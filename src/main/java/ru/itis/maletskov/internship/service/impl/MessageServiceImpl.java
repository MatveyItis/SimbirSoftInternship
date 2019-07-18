package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.repository.MessageRepository;
import ru.itis.maletskov.internship.service.MessageService;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Message saveMessage(Message message) {
        return null;
    }
}
