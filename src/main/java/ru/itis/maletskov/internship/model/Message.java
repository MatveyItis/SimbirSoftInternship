package ru.itis.maletskov.internship.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false, length = 1024)
    private String text;

    @Column(name = "sender")
    private String sender;
    
    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
