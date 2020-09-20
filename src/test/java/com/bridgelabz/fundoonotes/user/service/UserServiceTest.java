package com.bridgelabz.fundoonotes.user.service;

import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.exceptions.JWTException;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.user.repository.IUserRepository;
import com.bridgelabz.fundoonotes.user.service.implementation.UserService;
import com.bridgelabz.fundoonotes.utils.implementation.MailService;
import com.bridgelabz.fundoonotes.utils.implementation.Token;
import com.bridgelabz.fundoonotes.utils.template.EmailVerificationTemplate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

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

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

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

    @Test
    void givenUserLogin_WhenUnverifiedEmailID_ShouldThrowException() {
        LoginDTO loginDTO = new LoginDTO("kalyani@gmail.com", "Kalyani@123");
        UserDetails userDetails = new UserDetails(loginDTO);
        String message = "Please verify your email before proceeding";
        try {
            when(userRepository.findByEmailID(loginDTO.emailID)).thenReturn(java.util.Optional.of(userDetails));
            when(bCryptPasswordEncoder.matches(loginDTO.password, userDetails.getPassword())).thenReturn(true);
            userService.userLogin(loginDTO);
        } catch (UserServiceException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }

    @Test
    void givenUserLogin_WhenInvalidEmailID_ShouldThrowException() {
        LoginDTO loginDTO = new LoginDTO("kalyani@gmail.com", "kalyani@123");
        UserDetails userDetails = new UserDetails(loginDTO);
        String message = "INCORRECT EMAIL";
        try {
            when(bCryptPasswordEncoder.matches(loginDTO.password, userDetails.getPassword())).thenReturn(Boolean.valueOf(message));
            userService.userLogin(loginDTO);
        } catch (UserServiceException e) {
            Assert.assertEquals(message, e.getMessage());
        }
    }

    @Test
    void givenUserDetailsToLoginUser_WhenIncorrectPasswordEntered_ShouldThrowException() {
        LoginDTO loginDTO = new LoginDTO("kalyani@gmail.com","Kalyani@123");
        UserDetails userDetails = new UserDetails(loginDTO);
        userDetails.setVerified(true);
        try {

            when(userRepository.findByEmailID(loginDTO.emailID)).thenReturn(java.util.Optional.of(userDetails));
            when(bCryptPasswordEncoder.matches(loginDTO.password, userDetails.getPassword())).thenReturn(false);
            userService.userLogin(loginDTO);
        } catch (UserServiceException ex) {
            Assert.assertEquals("INCORRECT PASSWORD", ex.getMessage());
        }
    }

    @Test
    void givenUserDetails_WhenUserResetPassword_ShouldReturnResetPasswordLinkMessage() throws MessagingException {
        LoginDTO loginDTO = new LoginDTO("kalyani@gmail.com","kalyani123");
        UserDetails userDetails = new UserDetails(loginDTO);
        when(userRepository.findByEmailID(loginDTO.emailID)).thenReturn(java.util.Optional.of(userDetails));
        when(jwtToken.generateVerificationToken(any())).thenReturn(String.valueOf(userDetails));
        when(mailService.sendMail(any(),any(),any())).thenReturn("Mail has been send");
        String user = userService.resetPasswordLink("kalyani@gmail.com", "tokenuser");
        Assert.assertEquals("Reset Password Link Has Been Sent To Your Email Address",user);
    }

    @Test
    void givenUserDetails_WhenUserSetThePassword_ShouldReturnMessage() {
        String password="Kalyani@12345";
        String token="ghfd12hvw";
        String message = "Password Has Been Reset";
        LoginDTO loginDTO = new LoginDTO("kalyani@gmail.com","Kalyani@123");
        UserDetails userDetails = new UserDetails(loginDTO);

        when(jwtToken.decodeJWT(anyString())).thenReturn(1);
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(userDetails));
        when(bCryptPasswordEncoder.encode(password)).thenReturn(password);
        when(userRepository.save(any())).thenReturn(userDetails);
        String reset = userService.resetPassword(password,token);
        Assert.assertEquals(message,reset);
    }

    @Test
    void givenField_WhenCorrect_ShouldReturnMessage(){

        List<UserDetails> userDetailsList=new ArrayList<>();

        RegistrationDTO registrationDTO = new RegistrationDTO("kalyani","kalyanirane19@gmail.com","Kalyani@123","8855223366");
        UserDetails userDetails=new UserDetails(registrationDTO);
        userDetails.setVerified(true);

        String userField="verified";

        userDetailsList.add(userDetails);

        when(userRepository.findAllByVerified(true)).thenReturn(userDetailsList);

        List<UserDetails> allUsers = userService.getAllUsers(userField);

        Assert.assertEquals(userDetailsList,allUsers);

    }

}
