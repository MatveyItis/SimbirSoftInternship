package ru.itis.maletskov.internship.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.itis.maletskov.internship.dto.ServerResponseDto;
import ru.itis.maletskov.internship.model.MessageType;
import ru.itis.maletskov.internship.util.exception.ChatException;
import ru.itis.maletskov.internship.util.exception.CommandParsingException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class WSExceptionHandlerController {
    @MessageExceptionHandler({EntityNotFoundException.class, ChatException.class})
    @SendTo("/topic/messages/{chatId}")
    public ServerResponseDto handleError(@DestinationVariable Long chatId,
                                         RuntimeException e) {
        ServerResponseDto response = new ServerResponseDto();
        response.setType(MessageType.ERROR);
        response.setUtilMessage(e.getMessage());
        return response;
    }

    @MessageExceptionHandler({CommandParsingException.class})
    @SendTo("/topic/messages/{chatId}")
    public ServerResponseDto handleCommandError(@DestinationVariable Long chatId,
                                                RuntimeException e) {
        ServerResponseDto response = new ServerResponseDto();
        response.setType(MessageType.COMMAND_ERROR);
        response.setUtilMessage(e.getMessage());
        return response;
    }
}
