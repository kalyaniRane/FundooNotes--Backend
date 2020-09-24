package com.bridgelabz.fundoonotes.collaborator.service;

import com.bridgelabz.fundoonotes.collaborator.dto.CollaborateNoteDto;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;

import javax.mail.MessagingException;
import java.util.List;

public interface ICollaboratorService {
    String addCollaborator(CollaborateNoteDto collaborateNoteDto, UserDetails user) throws MessagingException;

    List<NoteDetails> getCollaboratorNotes(UserDetails user);

    String removeCollaboration(CollaborateNoteDto collaborateNoteDto);

}
