package com.bridgelabz.fundoonotes.utils.implementation;

import com.bridgelabz.fundoonotes.exceptions.JWTException;
import com.bridgelabz.fundoonotes.model.UserDetails;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import com.bridgelabz.fundoonotes.utils.IToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class Token implements IToken {

    @Autowired
    FileProperties jwtProperties;

    @Override
    public String generateVerificationToken(UserDetails userDetails) {

        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setId(String.valueOf(userDetails.getId()))
                .setSubject(userDetails.getFullName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(currentTime + jwtProperties.getVerificationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getJwtSecret())
                .compact();
    }

    @Override
    public int decodeJWT(String jwt) throws JWTException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getJwtSecret()).parseClaimsJws(jwt).getBody();

            return Integer.parseInt(claims.getId());
        } catch (ExpiredJwtException e) {
            throw new JWTException("session time out");
        }
    }

    @Override
    public String generateLoginToken(UserDetails userDetails) {

        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .setId(String.valueOf(userDetails.getId()))
                .setSubject(userDetails.getFullName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(currentTime + jwtProperties.getJwtExpirationMs()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getJwtSecret())
                .compact();
    }

}
