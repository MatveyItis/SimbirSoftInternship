package ru.itis.maletskov.internship.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        Optional<User> candidate = userRepository.findByLogin(login);
        if (candidate.isPresent()) {
            return new UserAuth(candidate.get());
        } else {
            throw new UsernameNotFoundException("User with login: " + login + " is not found");
        }
    }
}