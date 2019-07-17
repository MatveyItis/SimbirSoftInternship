package ru.itis.maletskov.internship.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    User createUser(UserForm userForm);
}
