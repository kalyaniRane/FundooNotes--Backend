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
        UserDetails userDetails = userRepository.findById(user.getId()).orElseThrow(()->new UserServiceException("User Not Found"));
        NoteDetails noteDetails = noteRepository.findById(collaborateNoteDto.getNoteID()).orElseThrow(() -> new NoteServiceException("Note Not Found"));
        UserDetails anotherUser = userRepository.findByEmailID(collaborateNoteDto.emailID).orElseThrow(()-> new UserServiceException("User Not Present"));

        if (noteDetails.getCollaborator_list().contains(anotherUser))
            throw new NoteServiceException("Note Collaborate Already");
        noteDetails.getCollaborator_list().add(0,anotherUser);
        noteDetails.getCollaborator_list().add(1,userDetails);
        userDetails.getCollaborateNotes().add(noteDetails);
        anotherUser.getCollaborateNotes().add(noteDetails);
        anotherUser.getNoteDetailsList().add(noteDetails);

        userRepository.save(userDetails);
        userRepository.save(anotherUser);

        String template= collaborationInvitationTemplate.getHeader(userDetails,noteDetails);
        mailService.sendMail(template,"Note Shared With You!!", anotherUser.getEmailID());
        noteRepository.save(noteDetails);
        return "Note Collaborate Successfully";
    }

    @Override
    public List<NoteDetails> getCollaboratorNotes(UserDetails user) {
        UserDetails userDetails = userRepository.findByEmailID(user.getEmailID()).orElseThrow(()-> new UserServiceException("User Not Found"));
        List<NoteDetails> collaborateNotes = userDetails.getCollaborateNotes();
        if (collaborateNotes.isEmpty())
            throw new NoteServiceException("No Collaborate Note");
        return collaborateNotes;
    }

    @Override
    public String removeCollaboration(CollaborateNoteDto collaborateNoteDto) {
        return null;
    }

}
