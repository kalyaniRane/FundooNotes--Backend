package com.bridgelabz.fundoonotes.user.service.implementation;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RedisUserDto;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.user.repository.IUserRepository;
import com.bridgelabz.fundoonotes.user.service.IUserService;
import com.bridgelabz.fundoonotes.utils.IToken;
import com.bridgelabz.fundoonotes.utils.implementation.MailService;
import com.bridgelabz.fundoonotes.utils.template.EmailVerificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    @Autowired
    FileProperties properties;

    private AmazonS3 s3client;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(properties.getAccess_key(), properties.getSecret_key());
        this.s3client = new AmazonS3Client(credentials);
    }

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
        requestURL ="http://localhost:8080/user/verify/mail/" + token;
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
                    return tokenString;
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
        urlToken = urlToken+"reset/"+ tokenGenerate;
        mailService.sendMail(urlToken, "Reset Password", user.getEmailID());
        return "Reset Password Link Has Been Sent To Your Email Address";
    }

    @Override
    public String resetPassword(String password, String urlToken) {
        int userId = jwtToken.decodeJWT(urlToken);
        UserDetails userDetails = userRepository.findById(userId).orElseThrow(() -> new UserServiceException("User Not Found"));
        String encodePassword = bCryptPasswordEncoder.encode(password);
        userDetails.setPassword(encodePassword);
        userDetails.setModified(LocalDateTime.now());
        userRepository.save(userDetails);
        return "Password Has Been Reset";
    }

    @Override
    public List<UserDetails> getAllUsers(String userField) {
        List<UserDetails> allByVerified = userField.equals("verified") ? userRepository.findAllByVerified(true) : userRepository.findAllByVerified(false);

        if(allByVerified.isEmpty()) throw new UserServiceException("User Not Available");
        else return allByVerified;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(properties.getBucket_name(), fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = properties.getEnd_point_url() + "/" + properties.getBucket_name() + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }
}
