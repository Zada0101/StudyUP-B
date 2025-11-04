package com.example.studyUp.service; // Services package


import com.example.studyUp.entity.User; // Entity
import java.util.Optional; // Optional type


public interface UserService { // User-related reads/helpers
    Optional<User> findByUsername(String username); // Lookup user
}