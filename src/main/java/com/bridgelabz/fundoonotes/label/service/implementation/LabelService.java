package com.bridgelabz.fundoonotes.label.service.implementation;

import com.bridgelabz.fundoonotes.exceptions.LabelServiceException;
import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.dto.MapDTO;
import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.label.repository.ILabelRepository;
import com.bridgelabz.fundoonotes.label.service.ILabelService;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.repository.INoteRepository;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class LabelService implements ILabelService {

    @Autowired
    ILabelRepository labelRepository;

    @Autowired
    INoteRepository noteRepository;


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
        LabelDetails labelDetails = labelRepository.findById(labelID).orElseThrow(() -> new NoteServiceException("Label Not Found"));
        labelDetails.setLabelName(labelName);
        labelDetails.setModified(LocalDateTime.now());
        labelRepository.save(labelDetails);
        return "Label Updated Successful";
    }

    @Override
    public String mapLabel(LabelDTO labelDTO, UserDetails user) {
        LabelDetails label = labelRepository.findByLabelName(labelDTO.labelName).orElseThrow(()-> new LabelServiceException("Label Not Found"));
            NoteDetails note = noteRepository.findById(labelDTO.noteID).orElseThrow(()-> new NoteServiceException("Note Not Found"));
        if(!note.isTrash() && !label.getNoteList().contains(note)){
            label.getNoteList().add(note);
            labelRepository.save(label);
            return "Label Created";
        }
            throw new NoteServiceException("Sorry,You Can't Create Label Here");
    }

    @Override
    public String removeNoteLabel(MapDTO mapDTO) {
        LabelDetails label = labelRepository.findById(mapDTO.labelID).orElseThrow(()-> new LabelServiceException("Label Not Found"));
        NoteDetails note = noteRepository.findById(mapDTO.noteID).orElseThrow(()-> new NoteServiceException("Note Not Found"));
        if(!note.isTrash()){
            label.getNoteList().remove(note);
            labelRepository.save(label);
            return "Label Removed";
        }
        throw new NoteServiceException("Sorry,You Can't Removed Label From Trash");
    }

}
