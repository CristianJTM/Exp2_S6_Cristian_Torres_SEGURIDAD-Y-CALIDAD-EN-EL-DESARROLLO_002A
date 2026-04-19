package com.unidos.animales.backend.repository;

import com.unidos.animales.backend.model.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

    List<Adoption> findByUserId(Long userId);

    List<Adoption> findByPetId(Long petId);

}