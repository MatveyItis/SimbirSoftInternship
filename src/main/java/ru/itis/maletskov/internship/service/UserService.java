package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.UserDto;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto createUser(UserForm userForm);

    UserDto renameUser(String username, String newUsername) throws InvalidAccessException;

    UserDto getUserById(Long id);
}
