package com.bridgelabz.fundoonotes.dto;


public class ResponseDTO {

    public String message;
    public Integer statusCode;

    public ResponseDTO(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
