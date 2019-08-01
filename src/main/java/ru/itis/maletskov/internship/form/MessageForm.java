package ru.itis.maletskov.internship.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.maletskov.internship.model.Message;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class MessageForm {
    private String text;
    private String sender;
    private Long chatId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;

    public static Message fromFormToMessage(MessageForm form) {
        Message message = new Message();
        message.setText(form.getText());
        message.setSender(form.getSender());
        message.setDateTime(form.getDateTime());
        return message;
    }
}
