package com.unidos.animales.backend.controller;

import com.unidos.animales.backend.model.Adoption;
import com.unidos.animales.backend.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @PostMapping
    public ResponseEntity<Adoption> createAdoption(@RequestBody Adoption adoption) {
        Adoption saved = adoptionService.createAdoption(adoption);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Adoption>> getAdoptions() {
        return ResponseEntity.ok(adoptionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adoption> getAdoption(@PathVariable Long id) {
        Adoption adoption = adoptionService.findById(id);
        return ResponseEntity.ok(adoption);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAdoption(@PathVariable Long id) {

        adoptionService.deleteAdoption(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Adopción eliminada correctamente");

        return ResponseEntity.ok(response);
    }
}
