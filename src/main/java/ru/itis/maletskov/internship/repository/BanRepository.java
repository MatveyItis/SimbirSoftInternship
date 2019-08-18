package ru.itis.maletskov.internship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.maletskov.internship.model.Ban;
import ru.itis.maletskov.internship.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
    List<Ban> findBansByUser(User user);

    Boolean existsBanByUserAndEndOfBannedAfter(User user, LocalDateTime timeNow);
}
