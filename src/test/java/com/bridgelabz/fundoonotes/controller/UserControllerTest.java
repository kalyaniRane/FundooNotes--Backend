package com.bridgelabz.fundoonotes.controller;


import com.bridgelabz.fundoonotes.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.service.implementation.UserService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(UserController.class)
@TestPropertySource(properties =
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    Gson gson = new Gson();

    @Test
    public void givenUser_WhenRegisterSuccessful_ShouldReturnTrue() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("kalyani","kalyanirane19@gmail.com","kalyani@123","8855223366");
        UserDetails userDetails=new UserDetails(registrationDTO);
        String stringConvertedDto = gson.toJson(userDetails);

        String message="REGISTRATION SUCCESSFUL";

        when(userService.userRegistration(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;

        Assert.assertEquals(message,responseMessage);

    }

    @Test
    public void givenUser_WhenNotRegisterSuccessful_ShouldReturnFalse() throws Exception {

        RegistrationDTO registrationDTO = new RegistrationDTO("kalyani","kalyanirane19@gmail.com","kalyani@123","8855223366");
        UserDetails userDetails=new UserDetails(registrationDTO);
        String stringConvertedDto = gson.toJson(userDetails);

        String message="REGISTRATION UNSUCCESSFUL";

        when(userService.userRegistration(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;

        Assert.assertEquals(message,responseMessage);
    }

}
