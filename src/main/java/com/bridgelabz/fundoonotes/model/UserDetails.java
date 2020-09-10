package com.bridgelabz.fundoonotes.model;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class UserDetails {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String emailID;
    @JsonIgnore
    private String password;

    private String mobileNumber;
    private String fullName;

    public UserDetails() {
    }


    public UserDetails(RegistrationDTO registrationDTO) {
        this.emailID = registrationDTO.emailID;
        this.password = registrationDTO.password;
        this.mobileNumber = registrationDTO.mobileNumber;
        this.fullName = registrationDTO.fullName;
    }
}
