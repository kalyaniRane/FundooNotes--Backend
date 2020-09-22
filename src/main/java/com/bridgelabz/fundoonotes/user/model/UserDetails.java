package com.bridgelabz.fundoonotes.user.model;

import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class UserDetails {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(unique = true,nullable = false)
    private String emailID;

    @Column(nullable = false)
    private String password;

    @Column(unique = true,nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private boolean  isVerified;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime modified;

    @OneToMany(mappedBy = "user")
    private List<NoteDetails> noteDetailsList;

    @JsonIgnore
    @ManyToMany()
    private List<NoteDetails> collaborateNotes;

    public UserDetails() {
    }


    public UserDetails(RegistrationDTO registrationDTO) {
        this.emailID = registrationDTO.emailID;
        this.password = registrationDTO.password;
        this.mobileNumber = registrationDTO.mobileNumber;
        this.fullName = registrationDTO.fullName;
        this.created=LocalDateTime.now();
        this.modified=LocalDateTime.now();
    }

    public UserDetails(LoginDTO logInDTO) {
        this.emailID=logInDTO.emailID;
        this.password=logInDTO.password;
    }
}
