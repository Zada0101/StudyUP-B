package com.study.StudyUp.service;



import com.study.StudyUp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

//imports JWT functions for creating, signing, and reading tokens.
//JWT->JSON Web Token.


//Tells Spring this class provides logic for JWTs
@Service
public class JwtService {

    //SECRET_KEY ->Used to sign and verify tokens
    //EXPIRATION_TIME->How long the token stays valid
    private final String SECRET_KEY = "mysecretkey"; // ⚠️ better move this to application.properties
    private final long EXPIRATION_TIME = 86400000;   // 1 day in ms


    //Builds a new token for a user
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    //extractUsername()-> Gets the username from a token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //validateToken()->Confirms if token is real and valid
    public boolean validateToken(String token, User user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    //isTokenExpired()->Finds out if the token is expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
