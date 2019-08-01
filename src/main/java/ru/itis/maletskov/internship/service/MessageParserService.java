package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.form.MessageForm;

public interface MessageParserService {
    ServerResponseDto parseMessage(MessageForm form);
}
