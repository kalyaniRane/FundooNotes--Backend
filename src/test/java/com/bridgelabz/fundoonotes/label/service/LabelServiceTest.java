package com.bridgelabz.fundoonotes.label.service;


import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.label.repository.ILabelRepository;
import com.bridgelabz.fundoonotes.label.service.implementation.LabelService;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.repository.INoteRepository;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.user.dto.RegistrationDTO;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class LabelServiceTest {

    @MockBean
    FileProperties fileProperties;

    @Mock
    ILabelRepository labelRepository;

    @InjectMocks
    LabelService labelService;

    @Mock
    INoteRepository noteRepository;

    @Test
    void givenData_WhenCorrect_ShouldReturnMessage(){
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(1);
        LabelDTO labelDTO=new LabelDTO("First Label");
        LabelDetails labelDetails=new LabelDetails();
        labelDetails.setUser(userDetails);
        BeanUtils.copyProperties(labelDTO,labelDetails);

        String message="LABEL CREATED";

        when(labelRepository.save(labelDetails)).thenReturn(labelDetails);
        String label = labelService.createLabel(labelDTO, userDetails);
        Assert.assertEquals(message,label);
    }

    @Test
    void  givenLabelID_WhenCorrect_ShouldReturnMessage(){
        LabelDetails labelDetails=new LabelDetails();
        Integer labelID=1;
        String message= "LABEL DELETED";

        when(labelRepository.findById(any())).thenReturn(java.util.Optional.of(labelDetails));
        String label = labelService.deleteLabel(labelID);

        Assert.assertEquals(message,label);
    }

    @Test
    void getAllLabels(){
        List<LabelDetails> labelDetailsList= new ArrayList<>();
        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(2);
        LabelDTO labelDTO=new LabelDTO("First Label");
        LabelDetails labelDetails=new LabelDetails();
        labelDetails.setUser(userDetails);
        labelDetails.setId(1);
        labelDetails.setUser(userDetails);
        BeanUtils.copyProperties(labelDTO,labelDetails);
        labelDetailsList.add(labelDetails);

        when(labelRepository.findAllByUser(any())).thenReturn(labelDetailsList);
        List<LabelDetails> allLabels = labelService.getAllLabels(userDetails);

        Assert.assertEquals(labelDetailsList,allLabels);
    }

    @Test
    void givenLabelData_WhenCorrect_ShouldReturnMessage(){

        LabelDTO labelDTO=new LabelDTO("First Label");
        LabelDetails labelDetails=new LabelDetails();
        BeanUtils.copyProperties(labelDTO,labelDetails);

        String labelName="First Label";
        Integer labelID=1;

        String message="Label Updated Successful";

        when(labelRepository.findById(any())).thenReturn(java.util.Optional.of(labelDetails));
        when(labelRepository.save(any())).thenReturn(labelDetails);
        String label = labelService.updateLabel(labelName, labelID);
        Assert.assertEquals(message,label);
    }

    @Test
    void givenLabel_WhenMapWithGivenNote_ShouldReturnMessage(){
        List<NoteDetails> noteDetailsList=new ArrayList();

        RegistrationDTO registrationDTO = new RegistrationDTO("Kalyani", "kalyani@gmail.com", "Kalyani@123", "8855885588");
        UserDetails userDetails = new UserDetails(registrationDTO);
        userDetails.setId(1);

        NoteDTO noteDTO=new NoteDTO(2,"First Note","This is my first note");
        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetailsList.add(noteDetails);

        LabelDTO labelDTO=new LabelDTO(2,"First Label");
        LabelDetails labelDetails=new LabelDetails();
        labelDetails.setNoteList(noteDetailsList);
        BeanUtils.copyProperties(labelDTO,labelDetails);


        String message="Label Created";

        when(labelRepository.findByLabelName(any())).thenReturn(java.util.Optional.of(labelDetails));
        when(noteRepository.findById(any())).thenReturn(java.util.Optional.of(noteDetails));
        when(labelRepository.save(any())).thenReturn(labelDetails);

        String label = labelService.mapLabel(labelDTO, userDetails);

        Assert.assertEquals(message,label);

    }

}
