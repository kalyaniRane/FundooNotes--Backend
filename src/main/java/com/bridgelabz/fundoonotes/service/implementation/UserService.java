package com.bridgelabz.fundoonotes.service.implementation;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.service.IUserService;
import com.bridgelabz.fundoonotes.utils.implementation.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Autowired
   MailService mailService;

    @Override
    public String userRegistration(RegistrationDTO registrationDTO) throws MessagingException {
        Optional<UserDetails> userDetail = userRepository.findByEmailID(registrationDTO.emailID);
        if (userDetail.isPresent()) {
            throw new UserServiceException("USER ALREADY EXISTS WITH THIS EMAIL ID");
        }
        String password = bCryptPasswordEncoder.encode(registrationDTO.password);
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setPassword(password);
        userRepository.save(userDetails);
        mailService.sendMail("Hii, Your registration Successful","Registration Successful",registrationDTO.emailID);
        return "Verification Mail Has Been Sent Successfully";
    }
}
