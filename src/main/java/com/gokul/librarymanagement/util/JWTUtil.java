package com.gokul.librarymanagement.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;

public  class JWTUtil {

    private static final long EXPIRY_DURATION = 1000*60*60;
    private static final String secret = "whatarethecausesandconsequencesofsoilerosion";
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

    public static String generateJWT(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRY_DURATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String extractUsername(String token){
        return getPayload(token).getSubject();
    }

    private static Claims getPayload(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public static boolean validateJWT(UserDetails userDetails, String token) {
        Claims claims = getPayload(token);

        String username = claims.getSubject();
        return userDetails.getUsername().equals(username) && isNotExpired(token);
    }

    private static boolean isNotExpired(String token){
        Claims claims = getPayload(token);
        return claims.getExpiration().after(new Date());
    }
}
