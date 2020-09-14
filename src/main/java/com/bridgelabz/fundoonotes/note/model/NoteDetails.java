package com.bridgelabz.fundoonotes.note.model;


import com.bridgelabz.fundoonotes.user.model.UserDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NoteDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;
    private LocalDateTime created=LocalDateTime.now();
    private boolean isTrash;

    @ManyToOne()
    @JoinColumn(name = "userID")
    private UserDetails user;

}

