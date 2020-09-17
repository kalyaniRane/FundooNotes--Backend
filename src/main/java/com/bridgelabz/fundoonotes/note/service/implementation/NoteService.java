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
import io.jsonwebtoken.JwtException;
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
    public String createNote(NoteDTO noteDTO, String token) {
        RedisUserModel byToken = redisUserRepository.findByToken(token);

        if (byToken.getToken().equals(token)){
            int userID = iToken.decodeJWT(token);
            UserDetails user = userRepository.findById(userID).orElseThrow(()->new UserServiceException("User Not Found"));
            NoteDetails noteDetails=new NoteDetails();
            BeanUtils.copyProperties(noteDTO,noteDetails);
            noteDetails.setUser(user);
            noteRepository.save(noteDetails);
            return "NEW NOTE CREATE";
        }
        throw new NoteServiceException("Token Not Found");
    }

    @Override
    public String trashNote(Integer noteID, String token) {
        RedisUserModel byToken = redisUserRepository.findByToken(token);
        if (byToken.getToken().equals(token)) {
            NoteDetails noteDetails = noteRepository.findById(noteID).orElseThrow(() -> new NoteServiceException("Note Not Found"));
            noteDetails.setTrash(true);
            noteRepository.save(noteDetails);
            return "Note Added In Trash";
        }
        throw new JWTException("Token Not Found");
    }

    @Override
    public String deleteNote(Integer noteID, String token) {
        RedisUserModel byToken = redisUserRepository.findByToken(token);
        if (byToken.getToken().equals(token)) {
            NoteDetails noteDetails = noteRepository.findById(noteID).orElseThrow(() -> new NoteServiceException("Note Not Found"));
            if(noteDetails.isTrash()){
                noteRepository.delete(noteDetails);
                return "Note Deleted Successfully";
            }
            throw new NoteServiceException("Note is Not in trash");
        }
        throw new JWTException("Token Not Found");
    }

    @Override
    public List<NoteDetails> getAllNotes(String token) {

        RedisUserModel byToken = redisUserRepository.findByToken(token);
        if (byToken.getToken().equals(token)) {
            int userID = iToken.decodeJWT(token);
           userRepository.findById(userID).orElseThrow(()->new UserServiceException("User Not Found"));
            List<NoteDetails> allByUserAndTrashFalse = noteRepository.findAllNotes(userID);
            if(!allByUserAndTrashFalse.isEmpty()){
                return allByUserAndTrashFalse;
            }
            throw new NoteServiceException("No Any Note Available");
        }
        throw new JWTException("Token Not Found");
    }

    @Override
    public String updateNote(NoteDTO noteDTO, String token) {
        RedisUserModel byToken = redisUserRepository.findByToken(token);
        if (byToken.getToken().equals(token)) {
            int userID = iToken.decodeJWT(token);
            userRepository.findById(userID).orElseThrow(() -> new UserServiceException("User Not Found"));
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
        throw new JWTException("Token Not Found");
    }

    @Override
    public List<NoteDetails> getAllNotesOfTrash(String token) {
        return null;
    }


}
