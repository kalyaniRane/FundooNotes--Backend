package com.bridgelabz.fundoonotes.collaborator.controller;


import com.bridgelabz.fundoonotes.collaborator.dto.CollaborateNoteDto;
import com.bridgelabz.fundoonotes.collaborator.service.ICollaboratorService;
import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/note")
public class CollaboratorController {

    @Autowired
    ICollaboratorService collaborateService;

    @PostMapping("/collaborator")
    public ResponseEntity<ResponseDTO> addCollaborator(@RequestHeader(value = "token",required = false) String token,@RequestBody CollaborateNoteDto collaborateNoteDto, HttpServletRequest request) throws MessagingException {
        UserDetails user = (UserDetails) request.getAttribute("user");
        String message=collaborateService.addCollaborator(collaborateNoteDto, user);
        ResponseDTO responseDTO = new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
