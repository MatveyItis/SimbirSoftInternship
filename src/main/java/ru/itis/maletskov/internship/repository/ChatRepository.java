package ru.itis.maletskov.internship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByName(String name);

    Boolean existsChatByName(String name);

    Boolean existsChatById(Long id);

    List<Chat> findChatsByMembersContains(User user);

    Long countByMembersContains(User user);
}
