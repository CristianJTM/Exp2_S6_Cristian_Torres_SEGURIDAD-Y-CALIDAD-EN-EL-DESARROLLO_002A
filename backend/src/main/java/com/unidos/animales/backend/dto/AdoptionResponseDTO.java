package com.unidos.animales.backend.dto;

public class AdoptionResponseDTO {
    private Long id;
    private String date;
    private String time;

    private Long userId;
    private String username;

    private Long petId;
    private String petName;
    private String petStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Long getPetId() {
        return petId;
    }
    public void setPetId(Long petId) {
        this.petId = petId;
    }
    public String getPetName() {
        return petName;
    }
    public void setPetName(String petName) {
        this.petName = petName;
    }
    public String getPetStatus() {
        return petStatus;
    }
    public void setPetStatus(String petStatus) {
        this.petStatus = petStatus;
    }



}
