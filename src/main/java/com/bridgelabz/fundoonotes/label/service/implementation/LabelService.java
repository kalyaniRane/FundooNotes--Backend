package com.bridgelabz.fundoonotes.label.service.implementation;

import com.bridgelabz.fundoonotes.exceptions.LabelServiceException;
import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.label.repository.ILabelRepository;
import com.bridgelabz.fundoonotes.label.service.ILabelService;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LabelService implements ILabelService {

    @Autowired
    ILabelRepository labelRepository;


    @Override
    public String createLabel(LabelDTO labelDTO, UserDetails user) {
        LabelDetails labelDetails=new LabelDetails();

        List<LabelDetails> allByUser = labelRepository.findAllByUser(user);
        List<LabelDetails> collect = allByUser.stream().filter(labelDetails1 -> labelDetails1.getLabelName().equals(labelDTO.labelName)).collect(Collectors.toList());

        if(!collect.isEmpty()){

            throw  new LabelServiceException("Label Already Exist");
        }
        BeanUtils.copyProperties(labelDTO,labelDetails);
        labelDetails.setUser(user);
        labelRepository.save(labelDetails);
        return "LABEL CREATED";

    }

    @Override
    public String deleteLabel(Integer labelID) {
        LabelDetails byId = labelRepository.findById(labelID).orElseThrow(()->new NoteServiceException("Label Not Found"));
        labelRepository.delete(byId);
        return "LABEL DELETED";
    }

    @Override
    public List<LabelDetails> getAllLabels(UserDetails user) {
        List<LabelDetails> allByUser = labelRepository.findAllByUser(user);

        return allByUser;
    }

    @Override
    public String updateLabel(String labelName, Integer labelID) {

        return "Label Updated Successful";
    }

}
