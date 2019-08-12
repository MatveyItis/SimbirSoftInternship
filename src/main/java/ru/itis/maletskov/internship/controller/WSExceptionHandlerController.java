package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.model.type.MessageType;
import ru.itis.maletskov.internship.service.MessageService;
import ru.itis.maletskov.internship.util.exception.ChatException;
import ru.itis.maletskov.internship.util.exception.CommandParsingException;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;
import ru.itis.maletskov.internship.util.exception.YBotException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class WSExceptionHandlerController {
    private final MessageService messageService;

    @MessageExceptionHandler({EntityNotFoundException.class, ChatException.class,
            CommandParsingException.class, YBotException.class, InvalidAccessException.class})
    @SendTo("/topic/messages/{chatId}")
    public ServerResponseDto handleErrors(@DestinationVariable Long chatId,
                                          @Payload MessageForm form,
                                          Exception e) throws Exception {
        ServerResponseDto response = new ServerResponseDto();
        form.setDateTime(LocalDateTime.now());
        form.setChatId(chatId);
        form.setType(MessageType.COMMAND);
        messageService.saveMessage(form);

        response.setMessage(MessageDto.fromFormToDto(form));
        response.getMessage().setType(MessageType.ERROR);
        response.setUtilMessage(e.getMessage());

        messageService.saveUtilMessage(response, chatId);
        return response;
    }
}
