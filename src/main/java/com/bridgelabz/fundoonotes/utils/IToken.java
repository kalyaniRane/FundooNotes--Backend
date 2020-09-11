package com.bridgelabz.fundoonotes.utils;

import com.bridgelabz.fundoonotes.exceptions.JWTException;
import com.bridgelabz.fundoonotes.model.UserDetails;

public interface IToken {

    int decodeJWT(String jwt) throws JWTException;
    String generateVerificationToken(UserDetails userDetails);

}
