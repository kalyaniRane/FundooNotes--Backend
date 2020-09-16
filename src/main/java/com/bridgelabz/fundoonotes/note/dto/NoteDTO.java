package com.bridgelabz.fundoonotes.note.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
@Setter
public class NoteDTO {

    @NotNull(message = "Please Enter NoteID")
    public Integer Id;
    @NotNull(message = "Please Enter Title")
    public String title;
    @NotNull(message = "Please Enter Description")
    public String description;

}
