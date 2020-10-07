package com.bridgelabz.fundoonotes.note.controller;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.elasticsearch.IElasticSearch;
import com.bridgelabz.fundoonotes.enums.SortedNotesEnum;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.dto.ReminderDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.service.INoteService;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/note")
@CrossOrigin(exposedHeaders = "Authorization")
public class NoteController {

    @Autowired
    INoteService noteService;

    @Autowired
    IElasticSearch elasticSearch;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createNote (@Valid @RequestBody NoteDTO noteDTO, @RequestHeader(value = "token",required = false) String token, BindingResult bindingResult, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        UserDetails user = (UserDetails) request.getAttribute("user");
        String note = noteService.createNote(noteDTO, user);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResponseDTO> trashNote (@RequestParam(value = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token){

        String message = noteService.trashNote(noteID);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);

    }

    @DeleteMapping("/trash")
    public ResponseEntity<ResponseDTO> deleteNote (@RequestParam(value = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token){
        String message=noteService.deleteNote(noteID);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ResponseDTO> getAllNotes(@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        List<NoteDetails> allNotes = noteService.getAllNotes(user);
        ResponseDTO responseDTO= new ResponseDTO("Notes Fetched",200,allNotes);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDTO> updateNote (@RequestBody NoteDTO noteDTO,@RequestHeader(value = "token",required = false) String token,HttpServletRequest request) throws IOException {
        UserDetails user = (UserDetails) request.getAttribute("user");
        String note = noteService.updateNote(noteDTO,user);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/trash/list")
    public ResponseEntity<ResponseDTO> getAllNotesOfTrash (@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        List<NoteDetails> allNotesOfTrash = noteService.getAllNotesOfTrash(user);
        ResponseDTO responseDTO = new ResponseDTO("Notes Fetched",200,allNotesOfTrash);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/sort/{sortedField}/{order}")
    public ResponseEntity<ResponseDTO> sortNotesByName(@PathVariable SortedNotesEnum sortedField,@PathVariable String order ,@RequestHeader(value = "token",required = false) String token, HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        List<NoteDetails> noteDetails = noteService.sortNotes(user,sortedField,order);
        ResponseDTO responseDTO = new ResponseDTO("Notes Fetched",200,noteDetails);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/pin")
    public ResponseEntity<ResponseDTO> pinNotes(@RequestParam(name = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        String note = noteService.pinNote(noteID, user);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/unpin")
    public ResponseEntity<ResponseDTO> unpinNotes(@RequestParam(name = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        String note = noteService.unpinNote(noteID, user);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/archive")
    public ResponseEntity<ResponseDTO> archieveNotes(@RequestParam(name = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        String note = noteService.archiveNote(noteID, user);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/unarchive")
    public ResponseEntity<ResponseDTO> unarchieveNotes(@RequestParam(name = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        String note = noteService.unarchiveNote(noteID, user);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/pin")
    public  ResponseEntity<ResponseDTO>getNoteOfPin(@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        List<NoteDetails> allNotesOfTrash = noteService.getAllNotesOfPin(user);
        ResponseDTO responseDTO = new ResponseDTO("Notes Fetched",200,allNotesOfTrash);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/archive")
    public  ResponseEntity<ResponseDTO>getNoteOfArchive(@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        List<NoteDetails> allNotesOfTrash = noteService.getAllNotesOfArchive(user);
        ResponseDTO responseDTO = new ResponseDTO("Notes Fetched",200,allNotesOfTrash);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/restore")
    public ResponseEntity<ResponseDTO> restoreNote (@RequestParam(value = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token){

        String message = noteService.restoreNote(noteID);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);

    }

    @PostMapping("/reminder")
    public ResponseEntity<ResponseDTO> addReminder(@RequestBody ReminderDTO reminderDTO, @RequestHeader(value = "token",required = false) String token, HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        String message=noteService.createReminder(reminderDTO,user);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/reminder")
    public ResponseEntity<ResponseDTO> removeReminder(@RequestParam(name = "noteID") Integer noteID, @RequestHeader(value = "token",required = false) String token, HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        String message=noteService.removeReminder(noteID,user);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/reminder")
    public ResponseEntity<ResponseDTO> getReminderList(@RequestHeader(value = "token",required = false) String token, HttpServletRequest request){
        UserDetails user = (UserDetails) request.getAttribute("user");
        List<NoteDetails> noteDetails=noteService.getReminderList(user);
        ResponseDTO responseDTO=new ResponseDTO("List Fetched",200,noteDetails);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO> searchNote(@RequestParam(name = "searchText") String searchText, @RequestHeader(value = "token",required = false) String token, HttpServletRequest request) throws IOException {
        UserDetails user = (UserDetails) request.getAttribute("user");
        List<NoteDetails> noteDetails = elasticSearch.searchNote(searchText, user);
        ResponseDTO responseDTO=new ResponseDTO("List Fetched",200,noteDetails);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

}
