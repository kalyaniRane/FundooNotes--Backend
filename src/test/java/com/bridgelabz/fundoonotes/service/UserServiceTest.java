package com.bridgelabz.fundoonotes.service;

import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.exceptions.JWTException;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.service.implementation.UserService;
import com.bridgelabz.fundoonotes.utils.implementation.MailService;
import com.bridgelabz.fundoonotes.utils.implementation.Token;
import com.bridgelabz.fundoonotes.utils.template.EmailVerificationTemplate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.mail.MessagingException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    IUserRepository userRepository;

    @InjectMocks
    UserService userService;

    @MockBean
    FileProperties fileProperties;

    @Mock
    Token jwtToken;

    @Mock
    MailService mailService;

    @Mock
    EmailVerificationTemplate verificationTemplate;


    @Test
    void givenUserDetails_WhenUserAlreadyPresent_ShouldThrowException() {
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        String message = "USER ALREADY EXISTS WITH THIS EMAIL ID";
        try {
            when(userRepository.findByEmailID(any())).thenReturn(java.util.Optional.of(userDetails));
            when(userRepository.save(any())).thenReturn(message);
            userService.userRegistration(registrationDTO, "url");
        } catch (UserServiceException | MessagingException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }


    @Test
    void givenUserDetails_WhenUserRegistered_ShouldReturnVerificationMessage() throws MessagingException {
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        String message="Verification Mail Has Been Sent Successfully";
        when(jwtToken.generateVerificationToken(any())).thenReturn(String.valueOf(userDetails));
        when(userRepository.findByEmailID(registrationDTO.emailID)).thenReturn(java.util.Optional.of(userDetails));
        when(userRepository.save(any())).thenReturn(userDetails);
        when(mailService.sendMail(any(),any(),any())).thenReturn(message);
        String user = userService.sendVerificationMail("kalyani@gmail.com", "tokenuser");
        Assert.assertEquals(message,user);
    }

    @Test
    void givenUserDetails_WhenUserNotRegistered_ShouldThrowException() throws MessagingException {
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        String message="User Not Found";
        try{
            when(jwtToken.generateVerificationToken(any())).thenReturn(String.valueOf(userDetails));
            when(userRepository.findByEmailID(registrationDTO.emailID)).thenReturn(java.util.Optional.of(userDetails));
            when(userRepository.save(any())).thenReturn(userDetails);
            when(mailService.sendMail(any(),any(),any())).thenReturn(message);
            userService.sendVerificationMail("kajal@gmail.com", "tokenuser");
        }catch (UserServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }


    }

    @Test
    void givenUserDetails_WhenUserVerifyEmail_ShouldReturnMessage() {
        String token="ghfd12hvw";
        String message = "User Has Been Verified";
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);

        when(jwtToken.decodeJWT(anyString())).thenReturn(1);
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(userDetails));
        when(userRepository.save(any())).thenReturn(userDetails);
        when(verificationTemplate.getHeader(any())).thenReturn(message);
        String verifyEmail = userService.verifyEmail(token);
        Assert.assertEquals(message,verifyEmail);
    }

    @Test
    void givenRequestToVerifyUser_WhenUserAlreadyVerifiedWithSameEmail_ShouldThrowException() {
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        String message="User With Same Email Id Already Exists";
        try {
            when(jwtToken.generateVerificationToken(any())).thenReturn(String.valueOf(userDetails));
            when(userRepository.findById(anyInt())).thenThrow(new JWTException("User With Same Email Id Already Exists"));
            when(userRepository.save(any())).thenReturn(userDetails);
            when(verificationTemplate.getHeader(any())).thenReturn(message);
            userService.verifyEmail("authorization");
        } catch (JWTException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }

}
