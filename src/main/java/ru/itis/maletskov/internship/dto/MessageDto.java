package ru.itis.maletskov.internship.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.maletskov.internship.model.Message;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageDto {
    private String text;
    private String sender;
    private String type;
    private Long chatId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    public static MessageDto fromMessageToDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setText(message.getText());
        messageDto.setSender(message.getSender());
        messageDto.setChatId(message.getChat().getId());
        messageDto.setDateTime(message.getDateTime());
        return messageDto;
    }
}
