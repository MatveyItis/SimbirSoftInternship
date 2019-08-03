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
import ru.itis.maletskov.internship.model.MessageType;
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
        messageService.saveMessage(form);
        response.setMessage(MessageDto.fromFormToDto(form));
        if (e instanceof EntityNotFoundException || e instanceof ChatException || e instanceof InvalidAccessException) {
            response.getMessage().setType(MessageType.ERROR);
        }
        if (e instanceof CommandParsingException || e instanceof YBotException) {
            response.getMessage().setType(MessageType.COMMAND_ERROR);
        }
        response.setUtilMessage(e.getMessage());
        MessageForm serverMessage = new MessageForm();
        serverMessage.setChatId(chatId);
        serverMessage.setType(response.getMessage().getType());
        serverMessage.setDateTime(LocalDateTime.now());
        serverMessage.setText(e.getMessage());
        messageService.saveMessage(serverMessage);
        return response;
    }
}
