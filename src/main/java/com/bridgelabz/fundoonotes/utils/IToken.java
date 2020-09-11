package com.bridgelabz.fundoonotes.utils;

import com.bridgelabz.fundoonotes.model.UserDetails;

public interface IToken {

    String generateVerificationToken(UserDetails userDetails);

}
