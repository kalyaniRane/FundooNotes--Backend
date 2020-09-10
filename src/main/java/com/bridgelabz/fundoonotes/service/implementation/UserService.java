package com.bridgelabz.fundoonotes.service.implementation;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Override
    public String userRegistration(RegistrationDTO registrationDTO, String requestURL) {
        return "HELLO";
    }
}
