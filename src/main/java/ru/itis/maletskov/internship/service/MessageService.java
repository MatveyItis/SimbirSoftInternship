package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.form.UtilMessageForm;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

import java.util.List;

public interface MessageService {
    MessageDto saveMessage(MessageForm form) throws Exception;

    void deleteMessageById(String username, Long id) throws InvalidAccessException;

    List<MessageDto> findAllMessages();

    MessageDto saveUtilMessage(UtilMessageForm form) throws Exception;
}
