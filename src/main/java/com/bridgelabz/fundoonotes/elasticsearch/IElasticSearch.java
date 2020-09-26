package com.bridgelabz.fundoonotes.elasticsearch;

import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;

import java.io.IOException;
import java.util.List;

public interface IElasticSearch {

    String createNote(NoteDetails noteDetails);
    String updateNote(NoteDetails note) throws IOException;
    String deleteNote(Integer noteID);

    List<NoteDetails> searchNote(String searchText, UserDetails user) throws IOException;

}
