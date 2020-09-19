package com.bridgelabz.fundoonotes.label.service;

import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.model.LabelDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;

import java.util.List;

public interface ILabelService {

    String createLabel(LabelDTO labelDTO, UserDetails user);

    String deleteLabel(Integer labelID);

    List<LabelDetails> getAllLabels(UserDetails user);

    String updateLabel(String labelName, Integer labelID);
}
