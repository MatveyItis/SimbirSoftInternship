package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.model.Role;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) {
        Optional<User> candidate = userRepository.findByLogin(login);
        if (candidate.isPresent()) {
            return new UserAuth(candidate.get());
        } else {
            throw new UsernameNotFoundException("User with login: " + login + " is not found");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserForm userForm) {
        User user = new User();
        user.setLogin(userForm.getLogin());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        return userRepository.save(user);
    }
}
