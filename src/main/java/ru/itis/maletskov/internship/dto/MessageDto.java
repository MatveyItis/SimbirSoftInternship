package ru.itis.maletskov.internship.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.maletskov.internship.form.MessageForm;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.model.type.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MessageDto {
    private String text;
    private String sender;
    private Long chatId;
    private MessageType type;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    public static MessageDto fromMessageToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setText(message.getText());
        if (message.getSender() != null) {
            dto.setSender(message.getSender().getLogin());
        }
        dto.setChatId(message.getChat().getId());
        dto.setType(message.getType());
        dto.setDateTime(message.getDateTime());
        return dto;
    }

    public static MessageDto fromFormToDto(MessageForm form) {
        MessageDto dto = new MessageDto();
        dto.setText(form.getText());
        dto.setSender(form.getSender());
        dto.setChatId(form.getChatId());
        dto.setType(form.getType());
        dto.setDateTime(form.getDateTime());
        return dto;
    }
}
