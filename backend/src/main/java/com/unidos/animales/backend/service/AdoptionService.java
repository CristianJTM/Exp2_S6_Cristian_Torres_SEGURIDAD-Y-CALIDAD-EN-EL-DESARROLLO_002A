package com.unidos.animales.backend.service;

import com.unidos.animales.backend.exception.BadRequestException;
import com.unidos.animales.backend.exception.ResourceNotFoundException;
import com.unidos.animales.backend.model.Adoption;
import com.unidos.animales.backend.model.Pet;
import com.unidos.animales.backend.model.User;
import com.unidos.animales.backend.repository.AdoptionRepository;
import com.unidos.animales.backend.repository.PetRepository;
import com.unidos.animales.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    public Adoption createAdoption(Adoption adoption) {

        if (adoption.getPet() == null || adoption.getPet().getId() == null) {
            throw new BadRequestException("Debe especificar una mascota");
        }

        if (adoption.getUser() == null || adoption.getUser().getId() == null) {
            throw new BadRequestException("Debe especificar un usuario");
        }

        Pet pet = petRepository.findById(adoption.getPet().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        User user = userRepository.findById(adoption.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!"DISPONIBLE".equalsIgnoreCase(pet.getStatus())) {
            throw new BadRequestException("La mascota no está disponible para adopción");
        }

        // 🔥 actualizar estado
        pet.setStatus("ADOPTADO");
        petRepository.save(pet);

        adoption.setPet(pet);
        adoption.setUser(user);

        return adoptionRepository.save(adoption);
    }

    public List<Adoption> findAll() {
        return adoptionRepository.findAll();
    }

    public Adoption findById(Long id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adopción no encontrada"));
    }

    public void deleteAdoption(Long id) {
        if (!adoptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Adopción no encontrada");
        }

        adoptionRepository.deleteById(id);
    }
}
