package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.maletskov.internship.dto.UserDto;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.model.Role;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.UserService;
import ru.itis.maletskov.internship.util.exception.ExceptionMessages;
import ru.itis.maletskov.internship.util.exception.InvalidAccessException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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

    @Override
    public UserDto renameUser(String username, String newUsername) throws InvalidAccessException {
        Optional<User> userCandidate = userRepository.findByLogin(username);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND_MESSAGE, username));
        }
        if (userRepository.existsUserByLogin(newUsername)) {
            throw new InvalidAccessException(String.format(ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE, newUsername));
        }
        userCandidate.get().setLogin(newUsername);
        return UserDto.fromUserToDto(userRepository.save(userCandidate.get()));
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> userCandidate = userRepository.findById(id);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException(String.format(ExceptionMessages.USER_WITH_ID_NOT_FOUND_MESSAGE, id));
        }
        return UserDto.fromUserToDto(userCandidate.get());
    }
}
