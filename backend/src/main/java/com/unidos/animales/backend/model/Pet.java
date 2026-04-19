package com.unidos.animales.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 50)
    private String status;

    @OneToMany(mappedBy = "pet")
    @JsonIgnore
    private List<Adoption> adoptions;

    public Pet(){}

    public Pet(Long id, String name, String type, Integer age, String status){
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.status = status;
    }

    public Pet(Long id, String name, String type, Integer age, String status, List<Adoption> adoptions){
        this.id = id;
        this.name = name;
        this.type = type;
        this.age = age;
        this.status = status;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
