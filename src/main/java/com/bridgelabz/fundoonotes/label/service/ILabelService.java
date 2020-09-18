package com.bridgelabz.fundoonotes.label.service;

import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.user.model.UserDetails;

public interface ILabelService {

    String createLabel(LabelDTO labelDTO, UserDetails user);

}
