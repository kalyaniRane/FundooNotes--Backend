package com.bridgelabz.fundoonotes.dto;


import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
public class RegistrationDTO {

    @NotNull
    @Pattern(regexp = "^[A-Za-z. ]+[ ]*[A-Za-z.]*$", message = "Please Enter Full Name")
    public final String fullName;

    @NotNull
    @Pattern(regexp = "^([a-zA-Z]{3,}([.|_|+|-]?[a-zA-Z0-9]+)?[@][a-zA-Z0-9]+[.][a-zA-Z]{2,3}([.]?[a-zA-Z]{2,3})?)$", message = "please Enter EmailID")
    public final String emailID;

    @NotNull
    @Pattern(regexp = "^((?=[^@|#|&|%|$]*[@|&|#|%|$][^@|#|&|%|$]*$)*(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9#@$?]{8,})$", message = "Please Enter Password")
    public final String password;

    @NotNull
    @Pattern(regexp = "^([6-9]{1}[0-9]{9})$", message = "Please Enter Mobile Number")
    public final String mobileNumber;

    public boolean isVerified;

}
