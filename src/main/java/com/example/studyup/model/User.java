package com.example.studyup.model;
// 👆 Make sure this matches your folder path exactly: src/main/java/com/example/studyUp/model/User.java

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ✅ Simple User model used in your service and controller layers.
 * This isn’t tied to a database yet — just a plain object for now.
 */
@Data                   // Auto-generates getters/setters, equals(), hashCode(), toString()
@NoArgsConstructor       // Auto-generates a no-args constructor
@AllArgsConstructor      // Auto-generates a constructor with all fields
@Builder                 // ✅ Enables `User.builder()` syntax (used in tests)
public class User {

    // ✅ Unique user ID (simulated for now)
    private Long id;

    // ✅ Unique username
    private String username;

    // ✅ Encrypted password (never store raw passwords)
    private String passwordHash;

    // ✅ Role — typically "USER" or "ADMIN"
    private String role;
}
