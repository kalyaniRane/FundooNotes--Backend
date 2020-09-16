package com.bridgelabz.fundoonotes.dto;


public class ResponseDTO {

    public String message;
    public Integer statusCode;
    public Object data;

    public ResponseDTO(String message, Integer statusCode, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public ResponseDTO(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
