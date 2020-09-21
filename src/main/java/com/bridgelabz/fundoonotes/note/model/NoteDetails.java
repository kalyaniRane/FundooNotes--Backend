package com.bridgelabz.fundoonotes.note.model;


import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(nullable = false)
    private LocalDateTime created=LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime modified=LocalDateTime.now();

    @Column(nullable = false)
    private boolean isTrash;

    @Column(nullable = false)
    private boolean isPin;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "userID")
    private UserDetails user;

    @JsonIgnore
    @ManyToMany(mappedBy = "noteList",cascade = CascadeType.REMOVE)
    private List<LabelDetails> labelList;

}

