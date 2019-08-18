package ru.itis.maletskov.internship.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.model.UserAuth;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.util.exception.ExceptionMessages;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        Optional<User> candidate = userRepository.findByLogin(login);
        if (!candidate.isPresent()) {
            throw new UsernameNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND_MESSAGE, login));
        }
        return new UserAuth(candidate.get());
    }
}
