package com.unidos.animales.backend.controller;

import com.unidos.animales.backend.dto.LoginRequest;
import com.unidos.animales.backend.model.User;
import com.unidos.animales.backend.repository.UserRepository;
import com.unidos.animales.backend.dto.LoginResponse;
import com.unidos.animales.backend.security.CustomUserDetailsService;
import com.unidos.animales.backend.security.JwtAuthenticationFilter;
import com.unidos.animales.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

import static com.unidos.animales.backend.security.Constants.LOGIN_URL;

@RestController
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(LOGIN_URL)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        // ❌ usuario no existe
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Usuario no encontrado"));
        }

        User user = userOpt.get();

        // ❌ contraseña incorrecta
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Contraseña incorrecta"));
        }

        // ❌ usuario deshabilitado
        if (!user.isEnabled()) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "Usuario deshabilitado"));
        }

        // ✅ generar token
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole().getName()
        );
        System.out.println("🔥 TOKEN GENERATED = " + token);

        // ✅ respuesta
        LoginResponse response = new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().getName()
        );

        return ResponseEntity.ok(response);

    }
}
