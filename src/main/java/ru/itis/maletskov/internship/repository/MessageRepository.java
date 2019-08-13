package ru.itis.maletskov.internship.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itis.maletskov.internship.model.Message;

import javax.transaction.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Override
    @Transactional
    @Modifying
    @Query(value = "delete from Message m where m.id = :id")
    void deleteById(Long id);
}
