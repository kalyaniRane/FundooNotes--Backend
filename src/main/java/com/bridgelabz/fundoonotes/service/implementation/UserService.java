package com.bridgelabz.fundoonotes.service.implementation;

import com.bridgelabz.fundoonotes.dto.LoginDTO;
import com.bridgelabz.fundoonotes.dto.RedisUserDto;
import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.service.IUserService;
import com.bridgelabz.fundoonotes.utils.IToken;
import com.bridgelabz.fundoonotes.utils.implementation.MailService;
import com.bridgelabz.fundoonotes.utils.template.EmailVerificationTemplate;
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

    @Autowired
    IToken jwtToken;

    @Autowired
    EmailVerificationTemplate verificationTemplate;

    @Autowired
    RedisUserService redisUserService;

    @Override
    public String userRegistration(RegistrationDTO registrationDTO, String requestURL) throws MessagingException {
        Optional<UserDetails> userDetail = userRepository.findByEmailID(registrationDTO.emailID);
        if (userDetail.isPresent()) {
            throw new UserServiceException("USER ALREADY EXISTS WITH THIS EMAIL ID");
        }
        String password = bCryptPasswordEncoder.encode(registrationDTO.password);
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setPassword(password);
        userRepository.save(userDetails);
        return sendVerificationMail(registrationDTO.emailID, requestURL);
    }


    @Override
    public String sendVerificationMail(String email, String requestURL) throws MessagingException {
        UserDetails user = userRepository.findByEmailID(email).orElseThrow(()->new UserServiceException("User Not Found"));
        String token = jwtToken.generateVerificationToken(user);
        requestURL = requestURL.substring(0, requestURL.lastIndexOf("s") - 1) + "/user/verify/email/" + token;
        String subject="Email Verification";
        mailService.sendMail(requestURL,subject,user.getEmailID());
        return "Verification Mail Has Been Sent Successfully";
    }

    @Override
    public String verifyEmail(String token) {
        int userId = jwtToken.decodeJWT(token);
        UserDetails user = userRepository.findById(userId).get();
        user.setVerified(true);
        userRepository.save(user);
        return verificationTemplate.getHeader(user.getFullName());
    }

    @Override
    public String userLogin(LoginDTO loginDTO) {
        Optional<UserDetails> userDetail = userRepository.findByEmailID(loginDTO.emailID);

        if (userDetail.isPresent()) {
            if(userDetail.get().isVerified()){
                boolean password = bCryptPasswordEncoder.matches(loginDTO.password, userDetail.get().getPassword());
                if (password) {
                    String tokenString = jwtToken.generateLoginToken(userDetail.get());
                    RedisUserDto redisUserDto=new RedisUserDto(tokenString);
                    redisUserService.saveData(redisUserDto);
                    return "LOGIN SUCCESSFUL";
                }
                throw new UserServiceException("INCORRECT PASSWORD");
            }
            throw new UserServiceException("Please verify your email before proceeding");
        }
        throw new UserServiceException("INCORRECT EMAIL");
    }

    @Override
    public String resetPasswordLink(String email, String urlToken) throws MessagingException {
        UserDetails user = userRepository.findByEmailID(email).orElseThrow(() -> new UserServiceException("User Not Found"));
        String tokenGenerate = jwtToken.generateVerificationToken(user);
        urlToken = urlToken.substring(0, urlToken.lastIndexOf("s") - 1) + "/user/reset/password/" + tokenGenerate;
        mailService.sendMail(urlToken, "Reset Password", user.getEmailID());
        return "Reset Password Link Has Been Sent To Your Email Address";
    }

}
