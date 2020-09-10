package com.bridgelabz.fundoonotes.dto;


public class ResponseDTO {

    public String message;
    public Object data;
    public Integer statusCode;

    public ResponseDTO(String message, Object data, Integer statusCode) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }
}
