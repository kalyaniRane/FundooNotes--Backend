package com.bridgelabz.fundoonotes.note.service;

import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;

import java.util.List;

public interface INoteService {

    String createNote(NoteDTO noteDTO, UserDetails user);
    String trashNote(Integer noteI);

    String deleteNote(Integer noteID);
    List<NoteDetails> getAllNotes(UserDetails user);

    String updateNote(NoteDTO noteDTO);

    List<NoteDetails> getAllNotesOfTrash(UserDetails user);
}

