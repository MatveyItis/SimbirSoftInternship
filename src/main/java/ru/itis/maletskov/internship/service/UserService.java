package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.dto.UserDto;
import ru.itis.maletskov.internship.form.UserForm;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto createUser(UserForm userForm);
}
