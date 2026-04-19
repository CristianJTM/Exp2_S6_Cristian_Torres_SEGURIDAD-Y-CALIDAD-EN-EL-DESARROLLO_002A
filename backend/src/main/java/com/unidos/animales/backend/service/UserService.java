package com.unidos.animales.backend.service;

import com.unidos.animales.backend.exception.BadRequestException;
import com.unidos.animales.backend.exception.ResourceNotFoundException;
import com.unidos.animales.backend.model.User;
import com.unidos.animales.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new BadRequestException("El username es obligatorio");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new BadRequestException("La contraseña es obligatoria");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestException("El username ya existe");
        }

        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }

        userRepository.deleteById(id);
    }
}