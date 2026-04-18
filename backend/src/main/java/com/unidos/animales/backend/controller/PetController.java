package com.unidos.animales.backend.controller;


import com.unidos.animales.backend.model.Pet;
import jakarta.validation.Valid;
import com.unidos.animales.backend.repository.PetRepository;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @PostMapping("/pets")
    public ResponseEntity<?> savePet(@Valid @RequestBody Pet pet) {
        try{
            Pet saved = petRepository.save(pet);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al registrar mascota: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @GetMapping("/pets")
    public ResponseEntity<?> getPets() {
        try{
            List<Pet> pets = (List<Pet>) petRepository.findAll();
            return ResponseEntity.ok(pets);
        }catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<?> getPet(@PathVariable Long id) {
        try{
            Optional<Pet> pet = petRepository.findById(id);
            if(pet.isPresent()){
                return ResponseEntity.ok(pet.get());
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Long id) {
        try{
            if(petRepository.existsById(id)){
                petRepository.deleteById(id);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Mascota eliminada exitosamente");
                return ResponseEntity.ok(response);
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al eliminar mascota: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/pets/{id}")
    public ResponseEntity<?> updatePet(@PathVariable @Min(1) Long id, @RequestBody Pet petDetails) {
        try{
            if (petDetails.getName() == null &&
                    petDetails.getType() == null &&
                    petDetails.getAge() == null &&
                    petDetails.getStatus() == null) {

                Map<String, String> error = new HashMap<>();
                error.put("message", "Debe enviar al menos un campo para actualizar");
                return ResponseEntity.badRequest().body(error);
            }

            Optional<Pet> pet = petRepository.findById(id);
            if(pet.isPresent()){
                Pet existingPet = pet.get();
                if (petDetails.getName() != null) {
                    existingPet.setName(petDetails.getName());
                }
                if (petDetails.getType() != null) {
                    existingPet.setType(petDetails.getType());
                }
                if (petDetails.getAge() != null){
                    existingPet.setAge(petDetails.getAge());
                }
                if (petDetails.getStatus() != null){
                    List<String> validStatus = List.of("DISPONIBLE", "ADOPTADO", "EN_PROCESO");

                    if (!validStatus.contains(petDetails.getStatus())) {
                        Map<String, String> error = new HashMap<>();
                        error.put("message", "Estado inválido");
                        return ResponseEntity.badRequest().body(error);
                    }
                    existingPet.setStatus(petDetails.getStatus());
                }
                Pet updated = petRepository.save(existingPet);
                return ResponseEntity.ok(updated);
            }else {
                return ResponseEntity.notFound().build();
            }

        }catch (Exception e){
            Map<String, String> error = new HashMap<>();
            error.put("message", "Error al actualizar mascota: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

}
