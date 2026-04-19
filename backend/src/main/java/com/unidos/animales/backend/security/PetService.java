package com.unidos.animales.backend.security;


import com.unidos.animales.backend.model.Pet;
import com.unidos.animales.backend.exception.*;
import com.unidos.animales.backend.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    private static final List<String> VALID_STATUS =
            List.of("DISPONIBLE", "ADOPTADO", "EN_PROCESO");

    public Pet savePet(Pet pet) {

        if (pet.getStatus() == null || !VALID_STATUS.contains(pet.getStatus())) {
            throw new BadRequestException("Estado inválido");
        }

        return petRepository.save(pet);
    }



    public List<Pet> findAll() {
        return petRepository.findAll();

    }

    public Pet findById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));
    }

    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mascota no encontrada");
        }

        petRepository.deleteById(id);
    }

    public Pet updatePet(Long id, Pet petDetails) {

        if (petDetails.getName() == null &&
                petDetails.getType() == null &&
                petDetails.getAge() == null &&
                petDetails.getStatus() == null) {

            throw new BadRequestException("Debe enviar al menos un campo para actualizar");
        }

        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        if (petDetails.getName() != null) {
            existingPet.setName(petDetails.getName());
        }

        if (petDetails.getType() != null) {
            existingPet.setType(petDetails.getType());
        }

        if (petDetails.getAge() != null) {
            existingPet.setAge(petDetails.getAge());
        }

        if (petDetails.getStatus() != null) {

            List<String> validStatus = List.of("DISPONIBLE", "ADOPTADO", "EN_PROCESO");

            if (!validStatus.contains(petDetails.getStatus())) {
                throw new BadRequestException("Estado inválido");
            }

            existingPet.setStatus(petDetails.getStatus());
        }

        return petRepository.save(existingPet);
    }
}
