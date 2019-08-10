package ru.itis.maletskov.internship.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.maletskov.internship.model.type.MessageType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UtilMessageForm {
    private MessageType type;
    private String utilMessage;
    private Long chatId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateTime;
}
