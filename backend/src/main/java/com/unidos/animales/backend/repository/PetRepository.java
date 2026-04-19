package com.unidos.animales.backend.repository;

import com.unidos.animales.backend.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
