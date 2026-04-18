package com.unidos.animales.backend.model;


import jakarta.persistence.*;

@Entity
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String hour;

}
