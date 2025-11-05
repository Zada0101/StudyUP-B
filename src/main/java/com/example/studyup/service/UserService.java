package com.example.studyup.service;

import com.example.studyup.dto.UserRegistrationDto;
import com.example.studyup.model.AppUser;

public interface UserService {
    AppUser registerUser(UserRegistrationDto dto);
}
