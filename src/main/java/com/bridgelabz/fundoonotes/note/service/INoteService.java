package com.bridgelabz.fundoonotes.note.service;

import com.bridgelabz.fundoonotes.enums.SortedNotesEnum;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.dto.ReminderDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;

import java.util.List;

public interface INoteService {

    String createNote(NoteDTO noteDTO, UserDetails user);
    String trashNote(Integer noteI);

    String deleteNote(Integer noteID);
    List<NoteDetails> getAllNotes(UserDetails user);

    String updateNote(NoteDTO noteDTO, UserDetails user);

    List<NoteDetails> getAllNotesOfTrash(UserDetails user);

    List<NoteDetails> sortNotes(UserDetails user, SortedNotesEnum notesEnum, String order);

    String pinNote(Integer noteID, UserDetails user);

    String unpinNote(Integer noteID, UserDetails user);

    String archiveNote(Integer noteID, UserDetails user);

    String unarchiveNote(Integer noteID, UserDetails user);

    List<NoteDetails> getAllNotesOfPin(UserDetails user);

    List<NoteDetails> getAllNotesOfArchive(UserDetails user);

    String restoreNote(Integer noteID);

    String createReminder(ReminderDTO reminderDTO, UserDetails user);

    String removeReminder(Integer noteID, UserDetails user);

    List<NoteDetails> getReminderList(UserDetails user);
}

