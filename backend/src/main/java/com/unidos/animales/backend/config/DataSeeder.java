package com.unidos.animales.backend.config;


import com.unidos.animales.backend.model.Adoption;
import com.unidos.animales.backend.model.Pet;
import com.unidos.animales.backend.model.Role;
import com.unidos.animales.backend.model.User;
import com.unidos.animales.backend.repository.AdoptionRepository;
import com.unidos.animales.backend.repository.PetRepository;
import com.unidos.animales.backend.repository.RoleRepository;
import com.unidos.animales.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PetRepository petRepository;
    private AdoptionRepository adoptionRepository;
    private PasswordEncoder passwordEncoder;

    @Value("duoc2026")
    private String seedPassword;

    public DataSeeder(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PetRepository petRepository,
            AdoptionRepository adoptionRepository,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.petRepository = petRepository;
        this.adoptionRepository = adoptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String hash = passwordEncoder.encode(seedPassword);

        // ROLES
        Role roleAdmin;
        Role roleUser;
        Role roleVet;

        if (roleRepository.count() == 0) {
            roleAdmin = roleRepository.save(new Role("admin", "Administrador"));
            roleVet = roleRepository.save(new Role("vet", "Veterinario"));
            roleUser = roleRepository.save(new Role("user", "Usuario"));
        } else {
            roleAdmin = roleRepository.findByName("admin").get();
            roleVet = roleRepository.findByName("vet").get();
            roleUser = roleRepository.findByName("user").get();
        }

        // USERS
        User admin;
        User normalUser;
        User vet;

        if (userRepository.count() == 0) {
            admin = userRepository.save(new User("admin", hash, roleAdmin, true));
            normalUser = userRepository.save(new User("user", hash, roleUser, true));
            vet = userRepository.save(new User("vet", hash, roleVet, true));
        } else {
            admin = userRepository.findByUsername("admin").get();
            normalUser = userRepository.findByUsername("user").get();
            vet = userRepository.findByUsername("vet").get();
        }

        // PETS
        Pet pet1;
        Pet pet2;

        if (petRepository.count() == 0) {
            pet1 = petRepository.save(new Pet("Max", "Perro", 5, "DISPONIBLE"));
            pet2 = petRepository.save(new Pet("Rocky", "Perro", 3, "ADOPTADO"));
        } else {
            pet1 = petRepository.findAll().iterator().next();
            pet2 = pet1;
        }

        // ADOPTION
        if (adoptionRepository.count() == 0) {
            adoptionRepository.save(new Adoption(pet2, normalUser, "19-04-2026", "16:40"));
        }
    }
}
