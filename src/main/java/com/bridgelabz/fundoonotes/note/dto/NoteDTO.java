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

    @NotNull
    public String title;
    @NotNull
    public String description;
}
