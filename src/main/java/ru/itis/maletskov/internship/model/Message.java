package ru.itis.maletskov.internship.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
@ToString(exclude = "chat")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 1024)
    private String text;

    @Column(name = "sender")
    private String sender;

    @OneToOne
    private Chat chat;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
