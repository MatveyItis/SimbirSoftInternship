package ru.itis.maletskov.internship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerResponseDto {
    private MessageDto message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ResponseDataDto responseData;
    private String utilMessage;
}
