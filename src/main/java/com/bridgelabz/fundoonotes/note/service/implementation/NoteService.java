package com.bridgelabz.fundoonotes.note.service.implementation;


import com.bridgelabz.fundoonotes.enums.SortedNotesEnum;
import com.bridgelabz.fundoonotes.exceptions.NoteServiceException;
import com.bridgelabz.fundoonotes.exceptions.UserServiceException;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.dto.ReminderDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.repository.INoteRepository;
import com.bridgelabz.fundoonotes.note.service.INoteService;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import com.bridgelabz.fundoonotes.user.repository.IUserRepository;
import com.bridgelabz.fundoonotes.user.repository.RedisUserRepository;
import com.bridgelabz.fundoonotes.utils.IToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        NoteDetails noteDetails = getNotesByID(noteID);
        noteDetails.setTrash(true);
        noteRepository.save(noteDetails);
        return "Note Added In Trash";

    }

    @Override
    public String deleteNote(Integer noteID){
        NoteDetails noteDetails = getNotesByID(noteID);
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
    public String updateNote(NoteDTO noteDTO, UserDetails user) {

        NoteDetails noteDetails = getNotesByID(noteDTO.id);
        if(!noteDetails.isTrash()){
            if(noteDetails.getUser().getId().equals(user.getId())){
                noteDetails.setTitle(noteDTO.title);
                noteDetails.setDescription(noteDTO.description);
                noteDetails.setModified(LocalDateTime.now());
                noteRepository.save(noteDetails);
                return "Note Updated Successful";
            }
            throw new UserServiceException("You Can't Access This Note");
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

    @Override
    public List<NoteDetails> sortNotes(UserDetails user, SortedNotesEnum notesEnum, String order) {

        List<NoteDetails> allNotes = getAllNotes(user);

        List<NoteDetails> noteDetails = notesEnum.sortedNotes(allNotes);

        if(order.equals("desc")){
            Collections.reverse(noteDetails);
        }

        return noteDetails;
    }

    @Override
    public String pinNote(Integer noteID, UserDetails user) {

        NoteDetails noteDetails = getNotesByID(noteID);
        if(!noteDetails.isTrash()){
            if(noteDetails.getUser().getId().equals(user.getId())){
                if(noteDetails.isArchive()){
                    noteDetails.setArchive(false);
                    noteDetails.setPin(true);
                    noteRepository.save(noteDetails);
                    return "Pinned";
                }
                noteDetails.setPin(true);
                noteRepository.save(noteDetails);
                return "Pinned";
            }
            throw new UserServiceException("You Can't Access This Note");
        }
        throw new NoteServiceException("Can't Pin In Trash");
    }

    @Override
    public String unpinNote(Integer noteID, UserDetails user) {
        NoteDetails noteDetails = getNotesByID(noteID);
        if(!noteDetails.isTrash()){
            if(noteDetails.getUser().getId().equals(user.getId())){
                noteDetails.setPin(false);
                noteRepository.save(noteDetails);
                return "Unpinned";
            }
            throw new UserServiceException("You Can't Access This Note");
        }
        throw new NoteServiceException("Can't Pin In Trash");
    }

    @Override
    public String archiveNote(Integer noteID, UserDetails user) {
        NoteDetails noteDetails = getNotesByID(noteID);
        if(!noteDetails.isTrash()){
            if(noteDetails.getUser().getId().equals(user.getId())){
                noteDetails.setArchive(true);
                noteRepository.save(noteDetails);
                return "Note Archive";
            }
            throw new UserServiceException("You Can't Access This Note");
        }
        throw new NoteServiceException("Can't Archive In Trash");
    }

    @Override
    public String unarchiveNote(Integer noteID, UserDetails user) {
        NoteDetails noteDetails = getNotesByID(noteID);
        if(!noteDetails.isTrash()){
            if(noteDetails.getUser().getId().equals(user.getId())){
                noteDetails.setArchive(false);
                noteRepository.save(noteDetails);
                return "Note Unarchive";
            }
            throw new UserServiceException("You Can't Access This Note");
        }
        throw new NoteServiceException("Can't Unarchive In Trash");
    }

    @Override
    public List<NoteDetails> getAllNotesOfPin(UserDetails user) {
        int userID=user.getId();
        List<NoteDetails> allByUserAndTrashFalse = noteRepository.findAllNotesOfPin(userID);
        if(!allByUserAndTrashFalse.isEmpty()){
            return allByUserAndTrashFalse;
        }
        throw new NoteServiceException("No Any Note Available");
    }

    @Override
    public List<NoteDetails> getAllNotesOfArchive(UserDetails user) {
        int userID=user.getId();
        List<NoteDetails> allByUserAndTrashFalse = noteRepository.findAllNotesOfArchive(userID);
        if(!allByUserAndTrashFalse.isEmpty()){
            return allByUserAndTrashFalse;
        }
        throw new NoteServiceException("No Any Note Available");
    }

    @Override
    public String restoreNote(Integer noteID) {
        NoteDetails noteDetails = getNotesByID(noteID);
        noteDetails.setTrash(false);
        noteRepository.save(noteDetails);
        return "Note Restored";
    }

    @Override
    public String createReminder(ReminderDTO reminderDTO, UserDetails user) {
        NoteDetails noteDetails = getNotesByID(reminderDTO.noteID);
        if(!noteDetails.isTrash()){
            if(noteDetails.getUser().getId().equals(user.getId())){
                Date date =reminderDTO.dateAndTime;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                String format = formatter.format(date);
                noteDetails.setReminder(format);
                noteRepository.save(noteDetails);
                return "REMINDER ADDED";
            }
            throw new UserServiceException("You Can't Access This Note");
        }
        throw new NoteServiceException("Can't Unarchive In Trash");
    }

    @Override
    public String removeReminder(Integer noteID, UserDetails user) {
        NoteDetails noteDetails = getNotesByID(noteID);
        if(!noteDetails.isTrash()){
            if(noteDetails.getUser().getId().equals(user.getId())){
                noteDetails.setReminder(null);
                noteRepository.save(noteDetails);
                return "REMINDER REMOVED";
            }
            throw new UserServiceException("You Can't Access This Note");
        }
        throw new NoteServiceException("Can't Unarchive In Trash");
    }

    @Override
    public List<NoteDetails> getReminderList(UserDetails user) {
        List<NoteDetails> allNotes = noteRepository.findAll();
        List<NoteDetails> reminderNote = allNotes.stream().filter(noteDetails -> noteDetails.getReminder() != null).collect(Collectors.toList());
        return reminderNote;
    }

    public NoteDetails getNotesByID(Integer noteID){
        NoteDetails noteDetails = noteRepository.findById(noteID).orElseThrow(() -> new NoteServiceException("Note Not Found"));
        return noteDetails;
    }


}
