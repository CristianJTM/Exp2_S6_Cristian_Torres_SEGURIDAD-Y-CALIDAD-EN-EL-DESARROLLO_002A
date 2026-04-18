package com.unidos.animales.backend.repository;

import com.unidos.animales.backend.model.Pet;
import org.springframework.data.repository.CrudRepository;

public interface PetRepository extends CrudRepository<Pet, Long> {

}
