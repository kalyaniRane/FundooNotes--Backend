package com.bridgelabz.fundoonotes.note.controller;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.elasticsearch.ElasticSearch;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptor;
import com.bridgelabz.fundoonotes.interceptor.NoteServiceInterceptorAppConfig;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.service.implementation.NoteService;
import com.bridgelabz.fundoonotes.properties.FileProperties;
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
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    @MockBean
    NoteServiceInterceptor noteServiceInterceptor;

    @MockBean
    NoteServiceInterceptorAppConfig noteServiceInterceptorAppConfig;

    @MockBean
    ElasticSearch elasticSearch;

    Gson gson = new Gson();

    @Test
    void givenData_WhenCorrect_ShouldReturnMessage() throws Exception {
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
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
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");

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

        when(noteService.trashNote(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(put("/note/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(noteID))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void givenNoteId_WhenAvailable_ShouldReturnMessage() throws Exception {
        Integer noteID=2;
        String message="Note Deleted Successfully";

        when(noteService.deleteNote(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(delete("/note/trash")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(noteID))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenData_WhenIncorrect_ShouldReturnMessage() throws Exception {
        NoteDTO noteDTO=new NoteDTO(2," ","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        String stringConvertedDto = gson.toJson(noteDetails);
        BindingResult bindingResult = mock(BindingResult.class);

        String message="Please Enter Title";

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
    void getAllNotes() throws Exception {
        List<NoteDetails> noteDetailsList=new ArrayList();
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);
        String message="Notes Fetched";

        when(noteService.getAllNotes(any())).thenReturn(noteDetailsList);
        MvcResult mvcResult = this.mockMvc.perform(get("/note")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();


        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void getAllNotes_ShouldReturnStatus() throws Exception {
        List<NoteDetails> noteDetailsList=new ArrayList();
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);

        when(noteService.getAllNotes(any())).thenReturn(noteDetailsList);
        int mvcResult = this.mockMvc.perform(get("/note")
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse().getStatus();

        Assert.assertEquals(200, mvcResult);
    }

    @Test
    void givenData_WhenCorrectToUpdate_ShouldReturnMessage() throws Exception {
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        String stringConvertedDto = gson.toJson(noteDetails);

        String message="NOTE UPDATED";

        when(noteService.updateNote(any(), any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(post("/note/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringConvertedDto)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void getAllNotesOfTrash() throws Exception {
        List<NoteDetails> noteDetailsList=new ArrayList();
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);
        String message="Notes Fetched";

        when(noteService.getAllNotesOfTrash(any())).thenReturn(noteDetailsList);
        MvcResult mvcResult = this.mockMvc.perform(get("/note/trash/list")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenTypeForSortNoteList_WhenCorrect_ShouldReturnList() throws Exception {
        List<NoteDetails> noteDetailsList=new ArrayList();
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);
        String message="Notes Fetched";

        when(noteService.sortNotes(any(),any(),any())).thenReturn(noteDetailsList);
        MvcResult mvcResult = this.mockMvc.perform(post("/note/sort/NAME/desc")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void givenNoteID_WhenCorrect_ShouldReturnMessage() throws Exception {

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Pinned";

        when(noteService.pinNote(any(),any())).thenReturn(message);


        MvcResult mvcResult = this.mockMvc.perform(post("/note/pin")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(2))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void givenNoteIDToUnpin_WhenCorrect_ShouldReturnMessage() throws Exception {

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Unpinned";

        when(noteService.unpinNote(any(),any())).thenReturn(message);

        MvcResult mvcResult = this.mockMvc.perform(post("/note/unpin")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(2))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void givenNoteIDToArchive_WhenCorrect_ShouldReturnMessage() throws Exception {

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Note Archive";

        when(noteService.archiveNote(any(),any())).thenReturn(message);

        MvcResult mvcResult = this.mockMvc.perform(post("/note/archive")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(2))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void givenNoteIDToUnarchive_WhenCorrect_ShouldReturnMessage() throws Exception {

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Note Unarchive";

        when(noteService.unarchiveNote(any(),any())).thenReturn(message);

        MvcResult mvcResult = this.mockMvc.perform(post("/note/unarchive")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(2))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void getAllPinnedNotes() throws Exception {

        List<NoteDetails> noteDetailsList=new ArrayList();
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);
        String message="Notes Fetched";

        when(noteService.getAllNotesOfPin(any())).thenReturn(noteDetailsList);

        MvcResult mvcResult = this.mockMvc.perform(get("/note/pin")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void getAllArchiveNotes() throws Exception {

        List<NoteDetails> noteDetailsList=new ArrayList();
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);
        String message="Notes Fetched";

        when(noteService.getAllNotesOfArchive(any())).thenReturn(noteDetailsList);

        MvcResult mvcResult = this.mockMvc.perform(get("/note/archive")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);

    }

    @Test
    void givenNoteIDToRestore_WhenCorrect_ShouldReturnMessage() throws Exception {

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Note Restored";

        when(noteService.restoreNote(any())).thenReturn(message);
        MvcResult mvcResult = this.mockMvc.perform(put("/note/restore")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(2))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenDataOfReminder_WhenCorrect_ShouldReturnMessage() throws Exception {

        Integer noteID=1;

        String message="REMINDER REMOVED";

        when(noteService.removeReminder(any(),any())).thenReturn(message);

        MvcResult mvcResult = this.mockMvc.perform(put("/note/reminder")
                .contentType(MediaType.APPLICATION_JSON)
                .param("noteID", String.valueOf(noteID))).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

    @Test
    void givenText_WhenAvailable_ShouldReturnNotes() throws Exception {

        List<NoteDetails> noteDetailsList=new ArrayList<>();
        String searchText="a";
        String message="List Fetched";
        when(elasticSearch.searchNote(any(),any())).thenReturn(noteDetailsList);

        MvcResult mvcResult = this.mockMvc.perform(get("/note/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("searchText", searchText)).andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        ResponseDTO responseDto = gson.fromJson(response, ResponseDTO.class);
        String responseMessage = responseDto.message;
        Assert.assertEquals(message, responseMessage);
    }

}
