package com.study.StudyUp.repository;

import com.study.StudyUp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email); // Must exist
    Optional<User> findByUsername(String username);
}
