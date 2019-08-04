package ru.itis.maletskov.internship.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.itis.maletskov.internship.model.type.CommandType;

@Getter
@Setter
@ToString
public class ResponseDataDto {
    private CommandType commandType;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String username;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Long chatId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String createdChatName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String removedChatName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String renamedChatName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ConnectedChatDto connectedChat;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String connectedUserLogin;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String disconnectedChatName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String disconnectedUserLogin;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer disconnectedMinuteCount;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String renamedUserLogin;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String bannedUserLogin;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer bannedMinuteCount;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String nominatedModeratorLogin;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String downgradedModeratorLogin;
}
