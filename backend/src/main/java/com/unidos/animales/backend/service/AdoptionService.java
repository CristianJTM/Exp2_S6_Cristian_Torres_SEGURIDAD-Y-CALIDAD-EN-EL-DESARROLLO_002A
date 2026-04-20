package com.unidos.animales.backend.service;

import com.unidos.animales.backend.dto.AdoptionRequestDTO;
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

    public Adoption createAdoption(AdoptionRequestDTO dto) {

        Pet pet = petRepository.findById(dto.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!"DISPONIBLE".equalsIgnoreCase(pet.getStatus())) {
            throw new BadRequestException("La mascota no está disponible para adopción");
        }

        pet.setStatus("ADOPTADO");
        petRepository.save(pet);

        Adoption adoption = new Adoption();
        adoption.setPet(pet);
        adoption.setUser(user);
        adoption.setDate(dto.getDate());
        adoption.setTime(dto.getTime());

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
