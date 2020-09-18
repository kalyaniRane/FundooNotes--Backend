package com.bridgelabz.fundoonotes.label.controller;

import com.bridgelabz.fundoonotes.dto.ResponseDTO;
import com.bridgelabz.fundoonotes.label.dto.LabelDTO;
import com.bridgelabz.fundoonotes.label.service.ILabelService;
import com.bridgelabz.fundoonotes.user.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    ILabelService labelService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createLabel (@Valid @RequestBody LabelDTO labelDTO, @RequestHeader(value = "token",required = false) String token, BindingResult bindingResult, HttpServletRequest request){
        System.out.println("controller");
        if(bindingResult.hasErrors()){
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        UserDetails user = (UserDetails) request.getAttribute("user");
        String label = labelService.createLabel(labelDTO, user);
        ResponseDTO responseDTO=new ResponseDTO(label,200);
        return new ResponseEntity(responseDTO, HttpStatus.OK);
    }

}
