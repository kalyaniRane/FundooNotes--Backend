package com.bridgelabz.fundoonotes.note.service;


import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.repository.INoteRepository;
import com.bridgelabz.fundoonotes.note.service.implementation.NoteService;
import com.bridgelabz.fundoonotes.properties.FileProperties;
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

    @Test
    void givenData_WhenCorrect_ShouldReturnMessage(){

        NoteDTO noteDTO=new NoteDTO("First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        String token="token";
        String message = "NEW NOTE CREATE";

        when(redisUserRepository.findByToken(token)).thenReturn(redisUserModel);
        when(redisUserModel.getToken()).thenReturn(token);
        when(jwtToken.decodeJWT(token)).thenReturn(2);
        when(userRepository.findById(2)).thenReturn(java.util.Optional.of(userDetails));
        when(noteRepository.save(noteDetails)).thenReturn(noteDetails);
        String note = noteService.createNote(noteDTO, token);
        Assert.assertEquals(message,note);

    }

    @Test
    void givenDataAndToken_WhenNotCorrect_ShouldThrowException(){

        NoteDTO noteDTO=new NoteDTO("First Note","This is my first note");
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
            noteService.createNote(noteDTO, token);
        }catch (NoteServiceException e){
            Assert.assertEquals(message,e.getMessage());
        }
    }

}
