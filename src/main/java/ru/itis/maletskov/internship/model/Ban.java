package ru.itis.maletskov.internship.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ban_list")
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "banned_user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "begin_of_ban")
    private LocalDateTime beginOfBanned;

    @Column(name = "end_of_ban")
    private LocalDateTime endOfBanned;
}
