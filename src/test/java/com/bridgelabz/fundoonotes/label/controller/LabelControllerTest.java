package com.bridgelabz.fundoonotes.label.controller;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptor;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptorAppConfig;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.dto.MapDTO;
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

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void givenLabelID_WhenCorrect_ShouldReturnMessage() throws Exception {
        Integer labelID=1;

        String message="Label Deleted";

        when(labelService.deleteLabel(any())).thenReturn(message);

        MvcResult mvcResult = this.mockMvc.perform(delete("/label/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .param("labelID", String.valueOf(labelID)))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void getAllLabels() throws Exception {
        List<LabelDetails> labelDetailsList = new ArrayList<>();
        LabelDTO labelDTO=new LabelDTO("First Label");
        LabelDetails labelDetails=new LabelDetails();
        BeanUtils.copyProperties(labelDTO,labelDetails);
        labelDetailsList.add(labelDetails);

        String message="Labels Fetched";

        when(labelService.getAllLabels(any())).thenReturn(labelDetailsList);
        MvcResult mvcResult = this.mockMvc.perform(get("/label")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenLabelData_WhenCorrect_ShouldReturnMessage() throws Exception {

        String labelName="First Label";
        Integer labelID=1;

        String message="Label Update Successful";

        when(labelService.updateLabel(any(),any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(put("/label/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .param("labelName", labelName)
                .param("labelID", String.valueOf(labelID))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void givenLabel_WhenMapWithGivenNote_ShouldReturnMessage() throws Exception {
        LabelDTO labelDTO=new LabelDTO(2,"First Label");
        LabelDetails labelDetails=new LabelDetails();
        BeanUtils.copyProperties(labelDTO,labelDetails);

        String stringConvertedDto = gson.toJson(labelDetails);
        String message="Label Created";

        when(labelService.mapLabel(any(),any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/label/map")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }
    @Test
    void givenData_WhenCorrect_ShouldReturnMessage() throws Exception {

        MapDTO mapDTO=new MapDTO(1,2);
        String stringConvertedDto = gson.toJson(mapDTO);
        String message="Label Removed";


        when(labelService.removeNoteLabel(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(put("/label/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }
}
