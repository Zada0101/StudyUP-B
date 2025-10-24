package com.study.StudyUp.service;

import com.study.StudyUp.DTOs.UserRegistrationDto;
import com.study.StudyUp.model.User;

public interface UserService {
    User registerUser(UserRegistrationDto dto);
    User findByUsername(String username);
    boolean existsByUsername(String username);
}
