package com.bridgelabz.fundoonotes.note.controller;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.note.controller.NoteController;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.service.implementation.NoteService;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(NoteController.class)
@TestPropertySource(properties =
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    NoteService noteService;

    @MockBean
    FileProperties fileProperties;

    Gson gson = new Gson();

    @Test
    void givenData_WhenCorrect_ShouldReturnMessage() throws Exception {
        NoteDTO noteDTO=new NoteDTO("First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        String stringConvertedDto = gson.toJson(noteDetails);

        String message="NOTE CREATE";

        when(noteService.createNote(any(),any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/note/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenData_WhenWrongData_ShouldReturn400Status() throws Exception {
        NoteDTO noteDTO=new NoteDTO("First Note","This is my first note");

        String message="NOTE CREATE";
        when(noteService.createNote(any(),any())).thenReturn(message);
        int mvcResult = this.mockMvc.perform(post("/note/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus();
        Assert.assertEquals(400, mvcResult);
    }

    @Test
    void givenNoteId_WhenCorrect_ShouldReturnMessage() throws Exception {
        Integer noteID=2;
        String message="Note Added In Trash";

        when(noteService.trashNote(any(),any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(put("/note/trash")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(noteID))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

}
