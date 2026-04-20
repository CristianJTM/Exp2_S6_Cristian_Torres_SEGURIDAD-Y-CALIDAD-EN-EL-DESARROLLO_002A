package com.unidos.animales.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdoptionController {

    @GetMapping("/adoptions")
    public String listPets() {
        return "adoptions";
    }

    @GetMapping("/adoptions/new")
    public String showCreateForm() {
        return "new_adoption";
    }
}
