package com.bridgelabz.fundoonotes.exceptions;


import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FundooNotesExceptionHandler {

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ResponseDTO> userServiceExceptionHandler(UserServiceException e) {
        ResponseDTO responseDto = new ResponseDTO(e.getMessage(),500);
        return new ResponseEntity<>(responseDto, HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(JWTException.class)
    public ResponseEntity userServiceExceptionHandler(JWTException e){
        ResponseDTO responseDTO = new ResponseDTO(e.getMessage(),500);
        return new ResponseEntity(responseDTO,HttpStatus.ALREADY_REPORTED);
    }

}
