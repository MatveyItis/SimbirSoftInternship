package ru.itis.maletskov.internship.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ServerResponseDto {
    private MessageDto message;
    private String utilMessage;
}
