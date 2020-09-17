package com.bridgelabz.fundoonotes.note.service.implementation;


import com.bridgelabz.fundoonotes.exceptions.JWTException;
import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.repository.INoteRepository;
import com.bridgelabz.fundoonotes.note.service.INoteService;
import com.bridgelabz.fundoonotes.user.model.RedisUserModel;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.user.repository.IUserRepository;
import com.bridgelabz.fundoonotes.user.repository.RedisUserRepository;
import com.bridgelabz.fundoonotes.utils.IToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService implements INoteService {

    @Autowired
    RedisUserRepository redisUserRepository;

    @Autowired
    IToken iToken;

    @Autowired
    IUserRepository userRepository;

    @Autowired
    INoteRepository noteRepository;

    @Override
    public String createNote(NoteDTO noteDTO, UserDetails user) {

        NoteDetails noteDetails=new NoteDetails();
        BeanUtils.copyProperties(noteDTO,noteDetails);
        noteDetails.setUser(user);
        noteRepository.save(noteDetails);
        return "NEW NOTE CREATE";
    }

    @Override
    public String trashNote(Integer noteID) {
        NoteDetails noteDetails = noteRepository.findById(noteID).orElseThrow(() -> new NoteServiceException("Note Not Found"));
        noteDetails.setTrash(true);
        noteRepository.save(noteDetails);
        return "Note Added In Trash";

    }

    @Override
    public String deleteNote(Integer noteID){
        NoteDetails noteDetails = noteRepository.findById(noteID).orElseThrow(() -> new NoteServiceException("Note Not Found"));
        if(noteDetails.isTrash()){
            noteRepository.delete(noteDetails);
            return "Note Deleted Successfully";
        }
        throw new NoteServiceException("Note is Not in trash");
    }

    @Override
    public List<NoteDetails> getAllNotes(UserDetails user) {

        int userID=user.getId();
        List<NoteDetails> allByUserAndTrashFalse = noteRepository.findAllNotes(userID);
        if(!allByUserAndTrashFalse.isEmpty()){
            return allByUserAndTrashFalse;
        }
        throw new NoteServiceException("No Any Note Available");

    }

    @Override
    public String updateNote(NoteDTO noteDTO) {

        NoteDetails noteDetails = noteRepository.findById(noteDTO.id).orElseThrow(() -> new NoteServiceException("Note Not Found"));
        if(!noteDetails.isTrash()){
            noteDetails.setTitle(noteDTO.title);
            noteDetails.setDescription(noteDTO.description);
            noteDetails.setModified(LocalDateTime.now());
            noteRepository.save(noteDetails);
            return "Note Updated Successful";
        }
        throw new NoteServiceException("Can't Edit In Trash");

    }

    @Override
    public List<NoteDetails> getAllNotesOfTrash(UserDetails user) {

        int userID=user.getId();
        List<NoteDetails> allByUserAndTrashFalse = noteRepository.findAllNotesOfTrash(userID);
        if(!allByUserAndTrashFalse.isEmpty()){
            return allByUserAndTrashFalse;
        }
        throw new NoteServiceException("No Any Note Available");

    }


}
