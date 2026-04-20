package com.unidos.animales.backend.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.unidos.animales.backend.security.Constants.*;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        System.out.println("🔥 FILTER START");
        System.out.println("PATH = " + request.getServletPath());

        try {
            System.out.println("AUTH = " + request.getHeader("Authorization"));
        } catch (Exception e) {
            System.out.println("❌ HEADER ERROR: " + e.getMessage());
        }
        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("PATH = " + request.getServletPath());
        System.out.println("METHOD = " + request.getMethod());
        System.out.println("AUTH HEADER = " + request.getHeader("Authorization"));
        System.out.println("AUTH CONTEXT BEFORE = " + SecurityContextHolder.getContext().getAuthentication());

        System.out.println("==== TOKEN DEBUG ====");
        System.out.println("HEADER = " + request.getHeader("Authorization"));
        System.out.println("FILTER PATH = " + request.getServletPath());
        System.out.println("SECURITY CONTEXT BEFORE = " + SecurityContextHolder.getContext().getAuthentication());



        String path = request.getServletPath();

        // ✅ 1. RUTAS PÚBLICAS (NO pasan por JWT)
        if (isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            System.out.println("🔥 TRY JWT PARSE");
            String header = request.getHeader(HEADER_AUTHORIZACION_KEY);

            // ✅ 2. SOLO validar si hay token
            if (header != null && header.startsWith(TOKEN_BEARER_PREFIX)) {

                String token = header.replace(TOKEN_BEARER_PREFIX, "").trim();
                System.out.println("TOKEN RAW = " + token);
                System.out.println("SECRET = " + SECRET);
                Claims claims = Jwts.parser()
                        .verifyWith((SecretKey) getSigningKey(SECRET))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                System.out.println("CLAIMS = " + claims);
                System.out.println("AUTHORITIES RAW = " + claims.get("authorities"));
                System.out.println("TYPE = " + claims.get("authorities").getClass());

                setAuthentication(claims);
                System.out.println("AUTH AFTER SET = " + SecurityContextHolder.getContext().getAuthentication());
            }

            // ✔ SI NO HAY TOKEN → simplemente sigue (Spring decide si permite o no)

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("💥 JWT GENERAL ERROR = " + e.getClass());
            System.out.println("💥 MSG = " + e.getMessage());
            e.printStackTrace();

            SecurityContextHolder.clearContext();

            filterChain.doFilter(request, response); // 👈 clave
        }
    }

    private void setAuthentication(Claims claims) {
        System.out.println("✔ AUTH USER = " + claims.getSubject());
        System.out.println("✔ AUTH ROLES = " + claims.get("authorities"));
        List<String> authorities = claims.get("authorities", List.class);

        if (authorities == null) {
            authorities = List.of("user");
        }

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .toList()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        System.out.println("✔ AUTH SET: " + auth);
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/login")
                || path.startsWith("/api/auth/login"); // 👈 ajusta si quieres público
    }
}


