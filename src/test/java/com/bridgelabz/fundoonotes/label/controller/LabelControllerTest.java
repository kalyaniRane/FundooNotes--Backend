package com.bridgelabz.fundoonotes.label.controller;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptor;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptorAppConfig;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.label.service.implementation.LabelService;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(LabelController.class)
@TestPropertySource(properties =
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FileProperties fileProperties;

    @MockBean
    NoteServiceInterceptor noteServiceInterceptor;

    @MockBean
    NoteServiceInterceptorAppConfig noteServiceInterceptorAppConfig;

    @MockBean
    LabelService labelService;

    Gson gson = new Gson();


    @Test
    void givenLabel_WhenCorrect_ShouldReturnMessage() throws Exception {
        LabelDTO labelDTO=new LabelDTO("First Label");
        LabelDetails labelDetails=new LabelDetails();
        BeanUtils.copyProperties(labelDTO,labelDetails);

        String stringConvertedDto = gson.toJson(labelDetails);
        String message="Label Created";

        when(labelService.createLabel(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/label/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

}
