package com.bridgelabz.fundoonotes.label.model;


import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LabelDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @Column(nullable = false)
    private String labelName;

    @Column(nullable = false)
    private LocalDateTime created=LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modified=LocalDateTime.now();

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "userID",nullable = false)
    private UserDetails user;

    @JsonIgnore
    @ManyToMany()
    private List<NoteDetails> noteList;

}
