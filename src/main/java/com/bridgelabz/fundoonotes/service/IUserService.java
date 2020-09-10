package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;

import java.net.http.HttpRequest;

public interface IUserService {

    String userRegistration(RegistrationDTO registrationDTO, String requestURL);

}
