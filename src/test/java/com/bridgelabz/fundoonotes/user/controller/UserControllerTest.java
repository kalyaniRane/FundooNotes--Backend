package com.bridgelabz.fundoonotes.user.controller;


import com.bridgelabz.fundoonotes.user.dto.LoginDTO;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.user.service.implementation.UserService;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(UserController.class)
@TestPropertySource(properties =
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    FileProperties fileProperties;

    Gson gson = new Gson();

    @Test
    public void givenUser_WhenRegisterSuccessful_ShouldReturnTrue() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("kalyani","kalyanirane19@gmail.com","Kalyani@123","8855223366");
        UserDetails userDetails=new UserDetails(registrationDTO);
        String stringConvertedDto = gson.toJson(userDetails);

        String message="REGISTRATION SUCCESSFUL";

        when(userService.userRegistration(any(),any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;

        Assert.assertEquals(message,responseMessage);

    }

    @Test
    public void givenUser_WhenNotRegisterSuccessful_ShouldReturnFalse() throws Exception {

        RegistrationDTO registrationDTO = new RegistrationDTO("kalyani","kalyanirane19@gmail.com","Kalyani@123","8855223366");
        UserDetails userDetails=new UserDetails(registrationDTO);
        String stringConvertedDto = gson.toJson(userDetails);

        String message="REGISTRATION UNSUCCESSFUL";

        when(userService.userRegistration(any(),any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;

        Assert.assertEquals(message,responseMessage);
    }

    @Test
    void givenUserLogin_WhenFieldsAreCorrect_ShouldReturnMessage() throws Exception {
        LoginDTO logInDTO = new LoginDTO("kalyanirane19@gmail.com", "Kalyani@123");
        UserDetails userDetails = new UserDetails(logInDTO);
        String stringConvertDTO = gson.toJson(userDetails);
        String message = "LOGIN SUCCESSFUL";
        when(userService.userLogin(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertDTO)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals("LOGIN SUCCESSFUL", responseMessage);
    }

    @Test
    void givenEmailId_WhenProper_ShouldReturnMessage() throws Exception {
        String emailID="kalyani@gmail.com";
        String message="Reset Password Link Has Been Sent To Your Email Address";
        when(userService.resetPasswordLink(any(),any())).thenReturn(message);
        MvcResult result = this.mockMvc.perform(post("/user/forget/password")
                .param("emailID", emailID)).andReturn();
        String response=result.getResponse().getContentAsString();
        ResponseDTO responseDTO = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDTO.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenPassword_WhenProper_ShouldReturnProperMessage() throws Exception {
        String password = "Kalyani@123";
        String urlToken = "gvfa454";
        String message = "Password Has Been Reset";
        when(userService.resetPassword(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/user/reset/password/")
                .param("password", password)
                .param("token", urlToken)).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }



}
