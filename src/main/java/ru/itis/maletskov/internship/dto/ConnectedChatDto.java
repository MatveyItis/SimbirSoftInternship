package ru.itis.maletskov.internship.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class ConnectedChatDto {
    private Long id;
    private String name;
    private Set<String> members;
    private Set<String> moderators;
    private String ownerLogin;
    private String adminLogin;

    public static ConnectedChatDto fromDtoToConnectedChatDto(ChatDto chatDto) {
        ConnectedChatDto dto = new ConnectedChatDto();
        dto.setId(chatDto.getId());
        dto.setName(chatDto.getName());
        dto.setOwnerLogin(chatDto.getOwner().getLogin());
        if (chatDto.getAdmin() != null) {
            dto.setAdminLogin(chatDto.getAdmin().getLogin());
        }
        Set<String> members = new HashSet<>();
        chatDto.getMembers().forEach(m -> members.add(m.getLogin()));
        dto.setMembers(members);
        Set<String> moderators = new HashSet<>();
        chatDto.getModerators().forEach(m -> moderators.add(m.getLogin()));
        dto.setModerators(moderators);
        return dto;
    }
}
