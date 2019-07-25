package ru.itis.maletskov.internship.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(name = "chat_name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_type")
    private ChatType type;

    @ManyToMany(mappedBy = "chat")
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> members = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> moderators = new HashSet<>();

    @OneToOne
    private User owner;

    @OneToOne
    private User admin;

}
