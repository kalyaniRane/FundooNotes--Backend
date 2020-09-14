package com.bridgelabz.fundoonotes.note.repository;

import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INoteRepository extends JpaRepository<NoteDetails, Integer> {
}
