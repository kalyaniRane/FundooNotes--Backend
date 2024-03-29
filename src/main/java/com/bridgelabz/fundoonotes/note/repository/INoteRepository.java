package com.bridgelabz.fundoonotes.note.repository;

import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface INoteRepository extends JpaRepository<NoteDetails, Integer> {

    @Query(value = "select * from note_details where userid = :userid and is_trash = false",nativeQuery = true)
    List<NoteDetails> findAllNotes(@Param("userid") Integer userid);

    @Query(value = "select * from note_details where userid = :userid and is_trash = true",nativeQuery = true)
    List<NoteDetails> findAllNotesOfTrash(@Param("userid") Integer userid);

    @Query(value = "select * from note_details where userid = :userid and is_pin = true",nativeQuery = true)
    List<NoteDetails> findAllNotesOfPin(@Param("userid") Integer userid);

    @Query(value = "select * from note_details where userid = :userid and is_archive = true",nativeQuery = true)
    List<NoteDetails> findAllNotesOfArchive(@Param("userid") Integer userid);

}
