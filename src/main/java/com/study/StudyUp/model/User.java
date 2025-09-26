package com.study.StudyUp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Optional but good practice to name table explicitly
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate primary key
    private Long id;

    @Column(nullable = false, unique = true, length = 50) // Unique and required username
    private String username;

    @Column(nullable = false, length = 255) // Store hashed password, not plain text
    private String passwordHash;

    // No-args constructor (required by JPA)
    public User() {
    }

    // Constructor with fields
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // Optional: toString() excluding password
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
