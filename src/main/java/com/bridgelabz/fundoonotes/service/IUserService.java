package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;

import javax.mail.MessagingException;
import java.net.http.HttpRequest;

public interface IUserService {

    String userRegistration(RegistrationDTO registrationDTO) throws MessagingException;

}
