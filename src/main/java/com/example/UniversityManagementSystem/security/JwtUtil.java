package com.example.UniversityManagementSystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 *
 * This is just a utility file which helps to,
 * 1. Generate the JWT token
 * 2. Extract the username from the token
 * 3. Validate the token and check if the token is expired or not.
 */

@Component
public class JwtUtil {

    // Signature of token is generated using this secret key.
    private static final String SECRET =
            "your-very-secret-key-that-is-long-enough-for-hs256";

    // Token validity: 24 hours
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 2 * 4;

    // Generate the signing key from the secret
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Method to generate a JWT token for a given user
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        String username = extractUsername(token);
        return username.equals((userDetails.getUsername())) && !isTokenExpired(token);
    }


    // Helper methods

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {

        // If the extracted date is before current data, then expired
        return extractClaims(token).getExpiration().before(new Date());
    }






}
