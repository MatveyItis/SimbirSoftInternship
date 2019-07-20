package ru.itis.maletskov.internship.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_name")
    private String name;

    @ManyToMany
    private Set<Message> messages = new HashSet<>();

    @ManyToMany
    private Set<User> members = new HashSet<>();

    @ManyToMany
    private Set<User> moderators = new HashSet<>();

    @OneToOne
    private User owner;

    @OneToOne
    private User admin;

}
