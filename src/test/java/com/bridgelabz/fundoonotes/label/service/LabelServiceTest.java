package com.bridgelabz.fundoonotes.label.service;


import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.label.repository.ILabelRepository;
import com.bridgelabz.fundoonotes.label.service.implementation.LabelService;
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

import static org.mockito.Mockito.*;

@SpringBootTest
public class LabelServiceTest {

    @MockBean
    FileProperties fileProperties;

    @Mock
    ILabelRepository labelRepository;

    @InjectMocks
    LabelService labelService;

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

}
