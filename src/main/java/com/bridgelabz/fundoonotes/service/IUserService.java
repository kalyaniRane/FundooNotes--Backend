package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;

import javax.mail.MessagingException;

public interface IUserService {

    String userRegistration(RegistrationDTO registrationDTO) throws MessagingException;

    public String sendVerificationMail(String email, String requestURL) throws MessagingException;
}
