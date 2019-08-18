package ru.itis.maletskov.internship.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.itis.maletskov.internship.model.User;

@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String login;
    private String password;

    public static UserDto fromUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
