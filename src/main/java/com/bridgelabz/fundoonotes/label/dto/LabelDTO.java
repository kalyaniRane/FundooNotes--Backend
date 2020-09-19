package com.bridgelabz.fundoonotes.label.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class LabelDTO {

    public Integer noteID;

    @NotNull(message = "Please Enter Title")
    public String labelName;

    public LabelDTO(String labelName) {
     this.labelName=labelName;
    }
}