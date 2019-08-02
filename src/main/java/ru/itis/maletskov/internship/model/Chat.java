package ru.itis.maletskov.internship.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "chat")
@ToString(exclude = "messages")
@EqualsAndHashCode(exclude = "messages")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_type")
    private ChatType type;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "chat")
    @JsonIgnore
    private Collection<Message> messages = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> members = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> moderators = new HashSet<>();

    @OneToOne
    private User owner;

    @OneToOne
    private User admin;

    @Column(name = "created_chat_date")
    private LocalDateTime createdChatDate;
}
