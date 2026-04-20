package com.unidos.animales.frontend.controller;

import com.unidos.animales.frontend.dto.LoginRequest;
import com.unidos.animales.frontend.dto.LoginResponse;
import com.unidos.animales.frontend.service.BackendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final BackendService backendService;

    public AuthController(BackendService backendService) {
        this.backendService = backendService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = backendService.login(request);
            return ResponseEntity.ok(response);

        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());

        } catch (ResourceAccessException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Backend no disponible");

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno");
        }
    }
}
