package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;

import javax.mail.MessagingException;

public interface IUserService {

    public String userRegistration(RegistrationDTO registrationDTO, String requestURL) throws MessagingException;

    public String sendVerificationMail(String email, String requestURL) throws MessagingException;
}
