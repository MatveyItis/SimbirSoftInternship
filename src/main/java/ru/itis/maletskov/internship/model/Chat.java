package ru.itis.maletskov.internship.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "chat_name")
    private String name;

    @ManyToMany(mappedBy = "chat")
    @JsonIgnore
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
