package com.gokul.librarymanagement.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JWTUtil {

    private static final long EXPIRY_DURATION = 1000*60*60;
    private static final String secret = "whatarethecausesandconsequencesofsoilerosion";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

    public String generateJWT(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRY_DURATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
