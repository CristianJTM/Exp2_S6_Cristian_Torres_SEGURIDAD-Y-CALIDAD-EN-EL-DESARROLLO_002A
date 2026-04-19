package com.unidos.animales.backend.service;

import com.unidos.animales.backend.exception.BadRequestException;
import com.unidos.animales.backend.exception.ResourceNotFoundException;
import com.unidos.animales.backend.model.Role;
import com.unidos.animales.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {

        if (role.getName() == null || role.getName().isBlank()) {
            throw new BadRequestException("El nombre del rol es obligatorio");
        }

        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new BadRequestException("El rol ya existe");
        }

        return roleRepository.save(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rol no encontrado");
        }

        roleRepository.deleteById(id);
    }
}