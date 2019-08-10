package ru.itis.maletskov.internship.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.itis.maletskov.internship.model.type.MessageType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
@EqualsAndHashCode(exclude = {"chat", "sender"})
@ToString(exclude = {"chat", "sender"})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 1024)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    private MessageType type;

    @OneToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @OneToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
