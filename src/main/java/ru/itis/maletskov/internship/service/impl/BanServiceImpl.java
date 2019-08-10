package ru.itis.maletskov.internship.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.maletskov.internship.model.Ban;
import ru.itis.maletskov.internship.model.User;
import ru.itis.maletskov.internship.repository.BanRepository;
import ru.itis.maletskov.internship.repository.UserRepository;
import ru.itis.maletskov.internship.service.BanService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BanServiceImpl implements BanService {
    private final BanRepository banRepository;
    private final UserRepository userRepository;

    @Override
    public Boolean isUserBannedAtTheTime(String username) {
        Optional<User> userCandidate = userRepository.findByLogin(username);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException("User with name := " + username + " is not found");
        }
        return banRepository.existsBanByUserAndEndOfBannedAfter(userCandidate.get(), LocalDateTime.now());
    }

    @Override
    public List<Ban> findAllUserBans(String username) {
        Optional<User> userCandidate = userRepository.findByLogin(username);
        if (!userCandidate.isPresent()) {
            throw new EntityNotFoundException("User with name := " + username + " is not found");
        }
        return banRepository.findBansByUser(userCandidate.get());
    }
}
