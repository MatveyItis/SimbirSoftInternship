package ru.itis.maletskov.internship.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.ChatType;
import ru.itis.maletskov.internship.util.comparator.MessageDateTimeComparator;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
public class ChatDto {
    private Long id;
    private String name;
    private ChatType type;
    private Set<UserDto> members;
    private Set<UserDto> moderators;
    private UserDto admin;
    private UserDto owner;
    private List<MessageDto> messages;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdChatDate;

    public static ChatDto fromChatToDto(Chat chat) {
        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());
        dto.setName(chat.getName());
        dto.setType(chat.getType());
        List<MessageDto> messages = new ArrayList<>();
        if (chat.getMessages() != null && !chat.getMessages().isEmpty()) {
            chat.getMessages().forEach(m -> messages.add(MessageDto.fromMessageToDto(m)));
            messages.sort(new MessageDateTimeComparator());
        }
        dto.setMessages(messages);
        Set<UserDto> users = new HashSet<>();
        chat.getMembers().forEach(c -> users.add(UserDto.fromUserToDto(c)));
        dto.setMembers(users);
        Set<UserDto> moderatorsDto = new HashSet<>();
        if (chat.getModerators() != null && !chat.getModerators().isEmpty()) {
            chat.getModerators().forEach(m -> moderatorsDto.add(UserDto.fromUserToDto(m)));
        }
        dto.setModerators(moderatorsDto);
        if (chat.getAdmin() != null) {
            dto.setAdmin(UserDto.fromUserToDto(chat.getAdmin()));
        }
        dto.setOwner(UserDto.fromUserToDto(chat.getOwner()));
        dto.setCreatedChatDate(chat.getCreatedChatDate());
        return dto;
    }
}
