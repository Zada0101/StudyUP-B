package com.example.studyup.service;

import com.example.studyup.dto.UserRegistrationDto;
import com.example.studyup.model.AppUser;

public interface AuthService {

    AppUser register(UserRegistrationDto dto);

    String login(String username, String rawPassword);
}
