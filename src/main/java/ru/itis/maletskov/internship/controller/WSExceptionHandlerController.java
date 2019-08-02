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
import ru.itis.maletskov.internship.util.exception.YBotException;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@RequiredArgsConstructor
public class WSExceptionHandlerController {
    private final MessageService messageService;

    @MessageExceptionHandler({EntityNotFoundException.class, ChatException.class})
    @SendTo("/topic/messages/{chatId}")
    public ServerResponseDto handleError(@DestinationVariable Long chatId,
                                         @Payload MessageForm form,
                                         RuntimeException e) {
        ServerResponseDto response = new ServerResponseDto();
        response.setMessage(MessageDto.fromFormToDto(form));
        response.getMessage().setType(MessageType.ERROR);
        response.setUtilMessage(e.getMessage());
        messageService.saveMessage(MessageForm.fromDtoToForm(response.getMessage()));
        return response;
    }

    @MessageExceptionHandler({CommandParsingException.class, YBotException.class})
    @SendTo("/topic/messages/{chatId}")
    public ServerResponseDto handleCommandError(@DestinationVariable Long chatId,
                                                @Payload MessageForm form,
                                                RuntimeException e) {
        ServerResponseDto response = new ServerResponseDto();
        response.setMessage(MessageDto.fromFormToDto(form));
        response.getMessage().setType(MessageType.COMMAND_ERROR);
        response.setUtilMessage(e.getMessage());
        messageService.saveMessage(MessageForm.fromDtoToForm(response.getMessage()));
        return response;
    }
}
