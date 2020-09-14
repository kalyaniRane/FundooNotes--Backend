package com.bridgelabz.fundoonotes.note.service;

import com.bridgelabz.fundoonotes.note.dto.NoteDTO;

public interface INoteService {

    String createNote(NoteDTO noteDTO,String token);

}
