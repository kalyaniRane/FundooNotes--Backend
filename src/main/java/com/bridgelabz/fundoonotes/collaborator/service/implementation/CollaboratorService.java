package com.bridgelabz.fundoonotes.collaborator.service.implementation;

import com.bridgelabz.fundoonotes.collaborator.dto.CollaborateNoteDto;
import com.bridgelabz.fundoonotes.collaborator.service.ICollaboratorService;
import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.repository.INoteRepository;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.user.repository.IUserRepository;
import com.bridgelabz.fundoonotes.utils.implementation.MailService;
import com.bridgelabz.fundoonotes.utils.template.CollaborationInvitationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;


@Service
public class CollaboratorService implements ICollaboratorService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    INoteRepository noteRepository;

    @Autowired
    MailService mailService;

    @Autowired
    CollaborationInvitationTemplate collaborationInvitationTemplate;

    @Override
    public String addCollaborator(CollaborateNoteDto collaborateNoteDto, UserDetails user) throws MessagingException {
        return "Note Collaborate Successfully";
    }

}
