package com.bridgelabz.fundoonotes.model;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class UserDetails {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Column(name = "emailID",unique = true)
    private String emailID;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    @Column(name = "mobileNumber",unique = true)
    private String mobileNumber;

    @NotNull
    private String fullName;

    @NotNull
    private boolean  isVerified;

    public UserDetails() {
    }


    public UserDetails(RegistrationDTO registrationDTO) {
        this.emailID = registrationDTO.emailID;
        this.password = registrationDTO.password;
        this.mobileNumber = registrationDTO.mobileNumber;
        this.fullName = registrationDTO.fullName;
    }

    public UserDetails(LoginDTO logInDTO) {
        this.emailID=logInDTO.emailID;
        this.password=logInDTO.password;
    }
}
