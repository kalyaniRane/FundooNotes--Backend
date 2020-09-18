package com.bridgelabz.fundoonotes.note.controller;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.enums.SortedNotesEnum;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
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
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    INoteService noteService;

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
    public ResponseEntity<ResponseDTO> updateNote (@RequestBody NoteDTO noteDTO,@RequestHeader(value = "token",required = false) String token,HttpServletRequest request){
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

}
