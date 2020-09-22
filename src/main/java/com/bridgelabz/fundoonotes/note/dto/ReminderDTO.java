package com.bridgelabz.fundoonotes.note.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReminderDTO {

    public Integer noteID;

    public Date dateAndTime;

}
