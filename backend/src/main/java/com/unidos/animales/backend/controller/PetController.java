package com.unidos.animales.backend.controller;


import com.unidos.animales.backend.model.Pet;
import com.unidos.animales.backend.service.PetService;
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

@RestController
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping("/pets")
    public ResponseEntity<?> savePet(@Valid @RequestBody Pet pet) {
        Pet saved = petService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getPets() {
        List<Pet> pets = petService.findAll();
        if (pets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pets);
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<Pet> getPet(@PathVariable Long id) {
        Pet pet = petService.findById(id);
        return ResponseEntity.ok(pet);
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Map<String, String>> deletePet(@PathVariable Long id) {
        petService.deletePet(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Mascota eliminada exitosamente");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/pets/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable @Min(1) Long id,
                                         @RequestBody Pet petDetails) {

        Pet updated = petService.updatePet(id, petDetails);
        return ResponseEntity.ok(updated);
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
