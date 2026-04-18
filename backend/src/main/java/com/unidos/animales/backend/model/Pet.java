package com.unidos.animales.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 50)
    private int age;

    @Column(nullable = false, length = 50)
    private boolean adopted;

    @OneToMany(mappedBy = "pet")
    private List<Adoption> adoptions;

    public Pet(){}

    public Pet(Long id, String name, String type, int age, boolean adopted){
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.adopted = adopted;
    }

    public Pet(Long id, String name, String type, int age, boolean adopted, List<Adoption> adoptions){
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.adopted = adopted;
        this.adoptions = adoptions;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isAdopted() {
        return adopted;
    }

    public void setAdopted(boolean adopted) {
        this.adopted = adopted;
    }
}
