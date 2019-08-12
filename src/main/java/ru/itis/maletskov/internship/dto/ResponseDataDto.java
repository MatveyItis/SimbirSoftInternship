package ru.itis.maletskov.internship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.itis.maletskov.internship.model.type.CommandType;

import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseDataDto {
    private CommandType commandType;
    private String username;
    private Long chatId;
    private String createdChatName;
    private String removedChatName;
    private String renamedChatName;
    private ConnectedChatDto connectedChat;
    private String connectedUserLogin;
    private String disconnectedChatName;
    private String disconnectedUserLogin;
    private Integer disconnectedMinuteCount;
    private String renamedUserLogin;
    private String bannedUserLogin;
    private Integer bannedMinuteCount;
    private String nominatedModeratorLogin;
    private String downgradedModeratorLogin;
    private String chatBotHelpInfo;
    private String yBotHelpInfo;
    private List<String> videoReferences;
    private String channelName;
    private String commentInfo;
}
