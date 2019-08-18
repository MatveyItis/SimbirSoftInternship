package ru.itis.maletskov.internship.service;

import ru.itis.maletskov.internship.model.Ban;

import java.util.List;

public interface BanService {
    Boolean isUserBannedAtTheTime(String username);

    List<Ban> findAllUserBans(String username);
}
