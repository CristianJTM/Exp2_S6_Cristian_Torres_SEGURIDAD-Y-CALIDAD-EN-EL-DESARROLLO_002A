package com.unidos.animales.backend.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 50)
    private String description;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Role(){

    }

    public Role(Long id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Role(Long id, String name, String description, List<User> users){
        this.id = id;
        this.name = name;
        this.description = description;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
