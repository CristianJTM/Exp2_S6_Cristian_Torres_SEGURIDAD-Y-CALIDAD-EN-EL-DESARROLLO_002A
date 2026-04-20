package com.unidos.animales.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {


    public String generateToken(String username, String role) {
        SecretKey key = Keys.hmacShaKeyFor(Constants.SECRET.getBytes());

        return Jwts.builder()
                .subject(username)
                .claim("authorities", List.of(role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día
                .signWith(key)
                .compact();
    }
}
