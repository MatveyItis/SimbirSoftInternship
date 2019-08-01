package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.form.MessageForm;

import java.util.List;

public interface MessageService {
    MessageDto saveMessage(MessageForm messageForm);

    void deleteMessageById(Long id);

    List<MessageDto> findAllMessages();
}
