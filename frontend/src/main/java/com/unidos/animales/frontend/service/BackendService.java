package com.unidos.animales.frontend.service;

import com.unidos.animales.frontend.dto.LoginRequest;
import com.unidos.animales.frontend.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BackendService {

    @Value("${backend.base-url}")
    private String backendUrl;

    private final RestTemplate restTemplate;

    public BackendService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse login(LoginRequest request) {
        String url = backendUrl + "/auth/login";

        return restTemplate.postForObject(
                url,
                request,
                LoginResponse.class
        );
    }
}
