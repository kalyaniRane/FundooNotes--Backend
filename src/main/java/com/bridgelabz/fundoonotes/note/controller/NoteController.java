package com.bridgelabz.fundoonotes.note.controller;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.note.dto.NoteDTO;
import com.bridgelabz.fundoonotes.note.model.NoteDetails;
import com.bridgelabz.fundoonotes.note.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    INoteService noteService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createNote (@Valid @RequestBody NoteDTO noteDTO, @RequestHeader(value = "token",required = false) String token, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        String note = noteService.createNote(noteDTO, token);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResponseDTO> trashNote (@RequestParam(value = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token){

        String message = noteService.trashNote(noteID, token);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);

    }

    @DeleteMapping("/trash")
    public ResponseEntity<ResponseDTO> deleteNote (@RequestParam(value = "noteID") Integer noteID,@RequestHeader(value = "token",required = false) String token){
        String message=noteService.deleteNote(noteID,token);
        ResponseDTO responseDTO=new ResponseDTO(message,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ResponseDTO> getAllNotes(@RequestHeader(value = "token",required = false) String token){
        List<NoteDetails> allNotes = noteService.getAllNotes(token);
        ResponseDTO responseDTO= new ResponseDTO("Notes Fetched",200,allNotes);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDTO> updateNote (@RequestBody NoteDTO noteDTO,@RequestHeader(value = "token",required = false) String token){
        String note = noteService.updateNote(noteDTO, token);
        ResponseDTO responseDTO=new ResponseDTO(note,200);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

}
