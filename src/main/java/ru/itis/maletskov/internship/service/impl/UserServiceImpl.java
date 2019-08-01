package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.dto.UserDto;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.model.Role;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>();
        users.forEach(u -> dtos.add(UserDto.fromUserToDto(u)));
        return dtos;
    }

    @Override
    public UserDto createUser(UserForm userForm) {
        User user = new User();
        user.setLogin(userForm.getLogin());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        return UserDto.fromUserToDto(userRepository.save(user));
    }
}
