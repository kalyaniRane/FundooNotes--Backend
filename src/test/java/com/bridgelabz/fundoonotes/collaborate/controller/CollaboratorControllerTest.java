package com.bridgelabz.fundoonotes.collaborate.controller;


import com.bridgelabz.fundoonotes.collaborator.controller.CollaboratorController;
import com.bridgelabz.fundoonotes.collaborator.dto.CollaborateNoteDto;
import com.bridgelabz.fundoonotes.collaborator.service.ICollaboratorService;
import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptor;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptorAppConfig;
import com.bridgelabz.fundoonotes.label.controller.LabelController;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.properties.FileProperties;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CollaboratorController.class)
@TestPropertySource(properties =
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class CollaboratorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ICollaboratorService collaborateService;

    @MockBean
    FileProperties fileProperties;

    @MockBean
    NoteServiceInterceptor noteServiceInterceptor;

    @MockBean
    NoteServiceInterceptorAppConfig noteServiceInterceptorAppConfig;

    Gson gson = new Gson();

    @Test
    void givenRequestToCollaborateNote_WhenCollaborate_ItShouldReturnSuccessMessage() throws Exception {

        CollaborateNoteDto collaborateNoteDto = new CollaborateNoteDto(3, "kdw@gmail.com");
        String toJson = gson.toJson(collaborateNoteDto);

        String message="Note Collaborate Successfully";

        when(collaborateService.addCollaborator(any(), any())).thenReturn(message);
        MvcResult mvcResult = mockMvc.perform(post("/note/collaborate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void getAllCollaboratorNote() throws Exception {

        List<NoteDetails> noteDetailsList=new ArrayList<>();

        String message="List Fetched";

        when(collaborateService.getCollaboratorNotes(any())).thenReturn(noteDetailsList);
        MvcResult mvcResult = this.mockMvc.perform(get("/note/collaborate")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

}
