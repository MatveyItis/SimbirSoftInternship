package ru.itis.maletskov.internship.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.maletskov.internship.dto.MessageDto;
import ru.itis.maletskov.internship.model.Message;
import ru.itis.maletskov.internship.model.type.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MessageForm {
    private String text;
    private String sender;
    private Long chatId;
    private MessageType type;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    public static Message fromFormToMessage(MessageForm form) {
        Message message = new Message();
        message.setText(form.getText());
        message.setType(form.getType());
        message.setDateTime(form.getDateTime());
        return message;
    }

    public static MessageForm fromDtoToForm(MessageDto dto) {
        MessageForm form = new MessageForm();
        form.setChatId(dto.getChatId());
        form.setType(dto.getType());
        form.setText(dto.getText());
        form.setSender(dto.getSender());
        form.setDateTime(dto.getDateTime());
        return form;
    }
}
