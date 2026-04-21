package com.unidos.animales.frontend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 🔒 Desactivar CSRF (API + fetch)
                .csrf(csrf -> csrf.disable())

                // 🔒 Sin sesiones (JWT)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔒 Permisos
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().permitAll()
                )

                // 🔒 Headers de seguridad (esto está bien 👍)
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        "default-src 'self'; " +
                                                "script-src 'self'; " +
                                                "style-src 'self'; " +
                                                "img-src 'self' data:; " +
                                                "font-src 'self'; " +
                                                "connect-src 'self' http://localhost:8081;" +
                                                "form-action 'self'; " +
                                                "frame-ancestors 'none'; " +
                                                "base-uri 'self'; " +
                                                "object-src 'none'; " +
                                                "media-src 'self'; " +
                                                "frame-src 'none';"
                                )
                        )
                        .frameOptions(frame -> frame.deny())
                );


        return http.build();
    }

    // ✅ Necesario para llamar al backend
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
