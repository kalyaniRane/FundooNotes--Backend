package com.bridgelabz.fundoonotes.controller;


import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> userRegistration(@RequestBody RegistrationDTO registrationDTO) {
        String registration = userService.userRegistration(registrationDTO);
        ResponseDTO responseDTO=new ResponseDTO(registration,null,200);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
