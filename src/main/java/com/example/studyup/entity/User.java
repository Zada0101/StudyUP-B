package com.example.studyup.entity; // Entities (DB tables)


import jakarta.persistence.*; // JPA annotations


@Entity // Marks this class as a JPA entity (maps to DB table)
@Table(name = "users", // Table name "users"
        uniqueConstraints = { // Unique indexes for fast lookups and preventing duplicates
                @UniqueConstraint(columnNames = "username"),

        })
public class User {
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id; // Unique row identifier


    @Column(nullable = false, length = 50) // Required column, max length
    private String username; // Unique username


    @Column(nullable = false) // Required column
    private String email; // Unique email


    @Column(nullable = false) // Required column
    private String passwordHash; // Hashed password (never store plain text)


    // Getters/setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPasswordHash() {return passwordHash;}
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}
}