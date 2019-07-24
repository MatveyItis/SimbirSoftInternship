package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.model.Message;

import java.util.List;

public interface MessageService {
    Message saveMessage(Message message);

    void deleteMessage(Message message);

    List<Message> findAllMessages();
}
