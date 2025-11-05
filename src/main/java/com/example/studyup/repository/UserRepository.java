package com.example.studyup.repository;
// 👆 This defines the package (folder) path.
// Make sure your file lives inside: src/main/java/com/example/studyUp/repository/

import com.example.studyup.entity.User;
// ✅ Import the User entity (table) so we can query it

import org.springframework.data.jpa.repository.JpaRepository;
// ✅ JpaRepository provides all common DB methods (findAll, save, delete, etc.)

import java.util.Optional;
// ✅ Optional is used when a user may or may not be found (prevents null errors)

/**
 * ✅ Repository layer: handles all communication with the database.
 * Spring automatically implements this interface at runtime.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // ✅ JpaRepository<User, Long> means:
    //    - User: the entity type
    //    - Long: the type of the primary key (id)

    /**
     * ✅ Finds a user by username.
     * Example: userRepository.findByUsername("nurzada");
     * Returns Optional<User> (safe for missing users).
     */
    Optional<User> findByUsername(String username);

    /**
     * ✅ Checks if a username already exists in DB.
     * Useful during registration to prevent duplicates.
     */
    boolean existsByUsername(String username);

    /**
     * ✅ Checks if an email already exists.
     * Used to ensure one account per email.
     */
    boolean existsByEmail(String email);
}
