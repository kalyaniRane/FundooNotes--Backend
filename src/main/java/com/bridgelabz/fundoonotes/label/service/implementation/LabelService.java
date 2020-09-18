package com.bridgelabz.fundoonotes.label.service.implementation;

import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.label.repository.ILabelRepository;
import com.bridgelabz.fundoonotes.label.service.ILabelService;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LabelService implements ILabelService {

    @Autowired
    ILabelRepository labelRepository;


    @Override
    public String createLabel(LabelDTO labelDTO, UserDetails user) {
        LabelDetails labelDetails=new LabelDetails();
        BeanUtils.copyProperties(labelDTO,labelDetails);
        labelDetails.setUser(user);
        labelRepository.save(labelDetails);
        return "LABEL CREATED";
    }

    @Override
    public String deleteLabel(Integer labelID) {
        return "LABEL DELETED";
    }

}