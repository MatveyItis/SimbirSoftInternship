package ru.itis.maletskov.internship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.itis.maletskov.internship.model.MessageType;

@Getter
@Setter
@ToString
public class ServerResponseDto {
    private MessageType type;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private MessageDto message;
    private String utilMessage;
}
