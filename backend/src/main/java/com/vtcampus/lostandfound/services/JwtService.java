
package com.vtcampus.lostandfound.services;

import com.vtcampus.lostandfound.domain.entities.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    // Temporary development secret. Move this to environment configuration
    // before deployment. Keep it at least 32 bytes long for HS256.
    private static final String SECRET =
            "VT-Campus-Lost-And-Found-Dev-Secret-Key-2026";

    private static final long EXPIRATION_TIME = 1000L * 60 * 60; // 1 hour

    private final SecretKey key = Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
    );

    public String generateToken(Users user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
