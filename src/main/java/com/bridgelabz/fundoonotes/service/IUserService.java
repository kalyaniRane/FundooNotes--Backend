package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.RegistrationDTO;

import javax.mail.MessagingException;

public interface IUserService {

    String userRegistration(RegistrationDTO registrationDTO, String requestURL) throws MessagingException;

    String sendVerificationMail(String email, String requestURL) throws MessagingException;

    String verifyEmail(String token);

    String userLogin(LoginDTO loginDTO);

    String resetPasswordLink(String email, String urlToken) throws MessagingException;

    String resetPassword(String password, String urlToken);
}
