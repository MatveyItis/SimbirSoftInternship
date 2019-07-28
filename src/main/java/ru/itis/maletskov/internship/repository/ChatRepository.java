package ru.itis.maletskov.internship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.maletskov.internship.model.Chat;
import ru.itis.maletskov.internship.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findChatByName(String name);

    Boolean existsChatByName(String name);

    List<Chat> findChatsByMembersContains(User user);

    @Query(value = "delete from chat where id = ?", nativeQuery = true)
    void deleteChatById(Long id);
}
