package com.bridgelabz.fundoonotes.user.controller;


import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> userRegistration(@Valid @RequestBody RegistrationDTO registrationDTO , BindingResult bindingResult, HttpServletRequest request) throws MessagingException {
        if (bindingResult.hasErrors()) {
            System.out.println("hi");
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        String registration = userService.userRegistration(registrationDTO,request.getHeader("Referer"));
        ResponseDTO responseDTO=new ResponseDTO(registration,200);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/verify/mail/{token}", produces = MediaType.TEXT_HTML_VALUE)
    public String verifyEmail(@PathVariable(name="token") String token){
        String verifyEmail = userService.verifyEmail(token);
        return verifyEmail;
    }

    @PostMapping("/login")
    public ResponseEntity userLogin(@Valid @RequestBody LoginDTO logInDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        String message = userService.userLogin(logInDTO);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/forget/password")
    public ResponseEntity getResetPassword(@RequestParam("emailID") String emailID,HttpServletRequest httpServletRequest) throws MessagingException {
        String resetPassword = userService.resetPasswordLink(emailID,httpServletRequest.getHeader("Referer"));
        ResponseDTO response = new ResponseDTO(resetPassword,200);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @PostMapping("/reset/password/")
    public ResponseEntity resetPassword(@RequestParam(name = "password") String password,@RequestParam(value = "token",defaultValue = "") String urlToken){
        String resetPassword = userService.resetPassword(password,urlToken);
        ResponseDTO response = new ResponseDTO(resetPassword,200);
        return new ResponseEntity(response,HttpStatus.OK);
    }

}