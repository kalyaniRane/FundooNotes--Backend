package com.bridgelabz.fundoonotes.note.service;


import com.bridgelabz.fundoonotes.enums.SortedNotesEnum;
import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.dto.ReminderDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.repository.INoteRepository;
import com.bridgelabz.fundoonotes.note.service.implementation.NoteService;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.RedisUserModel;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.user.repository.IUserRepository;
import com.bridgelabz.fundoonotes.user.repository.RedisUserRepository;
import com.bridgelabz.fundoonotes.utils.implementation.Token;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class NoteServiceTest {

    @Mock
    INoteRepository noteRepository;

    @InjectMocks
    NoteService noteService;

    @MockBean
    FileProperties fileProperties;

    @Mock
    Token jwtToken;

    @Mock
    RedisUserRepository redisUserRepository;

    @Mock
    RedisUserModel redisUserModel;

    @Mock
    IUserRepository userRepository;

    @Mock
    UserDetails userDetails;

    @Mock
    SortedNotesEnum notesEnum;

    @Test
    void givenData_WhenCorrect_ShouldReturnMessage(){

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        String message = "NEW NOTE CREATE";

        when(noteRepository.save(noteDetails)).thenReturn(noteDetails);
        String note = noteService.createNote(noteDTO,userDetails);
        Assert.assertEquals(message,note);

    }

    @Test
    void givenDataAndToken_WhenNotCorrect_ShouldThrowException(){

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        String token="token";
        String message = "Token Not Found";

        try{
            when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
            when(redisUserModel.getToken()).thenReturn("tokennn");
            when(jwtToken.decodeJWT(token)).thenReturn(2);
            when(userRepository.findById(2)).thenReturn(java.util.Optional.of(userDetails));
            when(noteRepository.save(noteDetails)).thenReturn(noteDetails);
            noteService.createNote(noteDTO,userDetails);
        }catch (NoteServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }
    }

    @Test
    void givenNoteId_WhenCorrect_ShouldReturnMeassage(){
        NoteDetails noteDetails=new NoteDetails();
        Integer noteId=2;
        String message="Note Added In Trash";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(noteDetails)).thenReturn(noteDetails);
        String note = noteService.trashNote(noteId);

        Assert.assertEquals(message,note);
    }

    @Test
    void givenNoteId_WhenIncorrect_ShouldThrowException(){
        NoteDetails noteDetails=new NoteDetails();
        Integer noteId=2;
        String token="token";
        String message="Note Not Found";

        try {
            when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
            when(redisUserModel.getToken()).thenReturn(token);
            when(noteRepository.findById(any())).thenThrow(new NoteServiceException("Note Not Found"));
            when(noteRepository.save(noteDetails)).thenReturn(noteDetails);
            noteService.trashNote(noteId);
        }catch (NoteServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }

    }

    @Test
    void givenToken_WhenIncorrect_ShouldThrowException(){
        NoteDetails noteDetails=new NoteDetails();
        Integer noteId=2;
        String token="token";
        String message="Token Not Found";

        try {
            when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
            when(redisUserModel.getToken()).thenReturn("tokennn");
            when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
            when(noteRepository.save(noteDetails)).thenReturn(noteDetails);
            noteService.trashNote(noteId);
        }catch (NoteServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }

    }

    @Test
    void givenNoteID_WhenAvailable_ShouldReturnMessage(){
        NoteDetails noteDetails=new NoteDetails();
        Integer noteId=2;
        noteDetails.setTrash(true);
        String message="Note Deleted Successfully";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        String note = noteService.deleteNote(noteId);

        Assert.assertEquals(message,note);

    }

    @Test
    void givenNoteID_WhenNotAvailable_ShouldReturnMessage(){
        NoteDetails noteDetails=new NoteDetails();
        Integer noteId=2;
        noteDetails.setTrash(true);
        String token="token";
        String message="Note Not Found";

        try{
            when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
            when(redisUserModel.getToken()).thenReturn(token);
            when(noteRepository.findById(any())).thenThrow(new NoteServiceException("Note Not Found"));
            noteService.deleteNote(noteId);
        }catch (NoteServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }
    }

    @Test
    void givenNoteID_WhenNotAvailableInTrash_ShouldThrowException(){
        NoteDetails noteDetails=new NoteDetails();
        Integer noteId=2;
        noteDetails.setTrash(false);
        String token="token";
        String message="Note is Not in trash";

        try{
            when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
            when(redisUserModel.getToken()).thenReturn(token);
            when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
            noteService.deleteNote(noteId);
        }catch (NoteServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }
    }

    @Test
    void givenToken_WhenNotCorrect_ShouldThrowException(){
        NoteDetails noteDetails=new NoteDetails();
        Integer noteId=2;
        noteDetails.setTrash(true);
        String token="token";
        String message="Token Not Found";

        try{
            when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
            when(redisUserModel.getToken()).thenReturn("tokennn");
            when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
            noteService.deleteNote(noteId);
        }catch (NoteServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }
    }

    @Test
    void givenToken_WhenCorrect_ShouldReturnNotes(){
        List<NoteDetails> noteDetailsList=new ArrayList();
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(2);
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);
        String token="token";

        when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
        when(redisUserModel.getToken()).thenReturn(token);
        when(jwtToken.decodeJWT(token)).thenReturn(2);
        when(userRepository.findById(2)).thenReturn(java.util.Optional.of(userDetails));
        when(noteRepository.findAllNotes(2)).thenReturn(noteDetailsList);
        List<NoteDetails> allNotes = noteService.getAllNotes(userDetails);
        Assert.assertEquals(noteDetailsList,allNotes);
    }

    @Test
    void givenData_WhenCorrectToUpdate_ShouldReturnMessage() throws IOException {
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(2);

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setUser(userDetails);
        BeanUtils.copyProperties(noteDTO,noteDetails);
        String message = "Note Updated Successful";

        when(noteRepository.findById(2)).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(noteDetails)).thenReturn(noteDetails);
        String note = noteService.updateNote(noteDTO, userDetails);
        Assert.assertEquals(message,note);
    }

    @Test
    void givenToken_WhenCorrect_ShouldReturnNotesOfTrash(){
        List<NoteDetails> noteDetailsList=new ArrayList();
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(2);
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);
        when(noteRepository.findAllNotesOfTrash(2)).thenReturn(noteDetailsList);
        List<NoteDetails> allNotes = noteService.getAllNotesOfTrash(userDetails);
        System.out.println(allNotes);
        Assert.assertEquals(noteDetailsList,allNotes);
    }


    @Test
    void givenSortType_WhenCorrect_ShouldReturnNotes(){
        List<NoteDetails> noteDetailsList=new ArrayList();
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(1);
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);

        when(noteRepository.findAllNotes(1)).thenReturn(noteDetailsList);
        when(noteService.getAllNotes(userDetails)).thenReturn(noteDetailsList);
        when(notesEnum.sortedNotes(any())).thenReturn(noteDetailsList);

        List<NoteDetails> noteDetails1 = noteService.sortNotes(userDetails, SortedNotesEnum.NAME, "desc");
        Assert.assertEquals(noteDetailsList,noteDetails1);

    }

    @Test
    void givenNoteID_WhenCorrect_ShouldReturnMessage(){
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(1);

        Integer noteID=2;
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setUser(userDetails);
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Pinned";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(any())).thenReturn(noteDetails);
        String note = noteService.pinNote(noteID, userDetails);
        Assert.assertEquals(message,note);

    }

    @Test
    void givenNoteIDToUnpin_WhenCorrect_ShouldReturnMessage(){
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(1);

        Integer noteID=2;
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setUser(userDetails);
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Unpinned";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(any())).thenReturn(noteDetails);
        String note = noteService.unpinNote(noteID, userDetails);

        Assert.assertEquals(message,note);

    }

    @Test
    void givenNoteIDToArchive_WhenCorrect_ShouldReturnMessage(){

        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(1);

        Integer noteID=2;
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setUser(userDetails);
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Note Archive";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(any())).thenReturn(noteDetails);

        String note = noteService.archiveNote(noteID, userDetails);
        Assert.assertEquals(message,note);

    }

    @Test
    void givenNoteIDToUnarchive_WhenCorrect_ShouldReturnMessage(){

        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(1);

        Integer noteID=2;
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setUser(userDetails);
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Note Unarchive";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(any())).thenReturn(noteDetails);

        String note = noteService.unarchiveNote(noteID, userDetails);
        Assert.assertEquals(message,note);

    }

    @Test
    void getAllPinnedNotes(){

        List<NoteDetails> noteDetailsList=new ArrayList();
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(2);
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setPin(true);
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);

        when(noteRepository.findAllNotesOfPin(any())).thenReturn(noteDetailsList);
        List<NoteDetails> allNotesOfPin = noteService.getAllNotesOfPin(userDetails);
        Assert.assertEquals(noteDetailsList,allNotesOfPin);
    }

    @Test
    void getAllArchiveNotes(){

        List<NoteDetails> noteDetailsList=new ArrayList();
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(2);
        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setArchive(true);
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);

        when(noteRepository.findAllNotesOfArchive(any())).thenReturn(noteDetailsList);
        List<NoteDetails> allNotesOfArchive = noteService.getAllNotesOfArchive(userDetails);
        Assert.assertEquals(noteDetailsList,allNotesOfArchive);
    }

    @Test
    void givenNoteIDToRestore_WhenCorrect_ShouldReturnMessage(){

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);

        String message="Note Restored";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(any())).thenReturn(noteDetails);

        String note = noteService.restoreNote(2);
        Assert.assertEquals(message,note);
    }

    @Test
    void givenReminderData_WhenCorrect_ShouldReturnMessage(){

        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(2);

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        noteDetails.setUser(userDetails);
        BeanUtils.copyProperties(noteDTO,noteDetails);

        ReminderDTO reminderDTO=new ReminderDTO(2,new Date());

        String message="REMINDER ADDED";

        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(noteRepository.save(any())).thenReturn(noteDetails);

        String reminder = noteService.createReminder(reminderDTO, userDetails);
        Assert.assertEquals(message,reminder);

    }

}
