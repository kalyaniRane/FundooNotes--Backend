package com.bridgelabz.fundoonotes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDTO {

    public String email;
    public String subject;
    public String message;

}
