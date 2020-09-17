package com.bridgelabz.fundoonotes.note.service;

import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;

import java.util.List;

public interface INoteService {

    String createNote(NoteDTO noteDTO,String token);
    String trashNote(Integer noteID,String token);

    String deleteNote(Integer noteID, String token);
    List<NoteDetails> getAllNotes(String token);

    String updateNote(NoteDTO noteDTO, String token);

    List<NoteDetails> getAllNotesOfTrash(String token);
}
