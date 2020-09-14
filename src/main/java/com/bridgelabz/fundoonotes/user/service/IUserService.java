package com.bridgelabz.fundoonotes.user.service;

import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;

import javax.mail.MessagingException;

public interface IUserService {

    String userRegistration(RegistrationDTO registrationDTO, String requestURL) throws MessagingException;

    String sendVerificationMail(String email, String requestURL) throws MessagingException;

    String verifyEmail(String token);

    String userLogin(LoginDTO loginDTO);

    String resetPasswordLink(String email, String urlToken) throws MessagingException;

    String resetPassword(String password, String urlToken);
}
