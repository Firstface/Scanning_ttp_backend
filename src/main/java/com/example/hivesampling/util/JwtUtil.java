package com.example.hivesampling.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey signingKey;
    private final long expiration;

    public JwtUtil(com.example.hivesampling.config.AppProperties appProperties) {
        String secret = appProperties.auth.jwt.secret;
        if (secret != null && secret.length() >= 32) {
            this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        } else {
            this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        this.expiration = appProperties.auth.jwt.expiration;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
