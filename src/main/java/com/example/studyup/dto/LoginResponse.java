package com.example.studyup.dto; // DTO package


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token; // Demo token string (replace with real JWT later)
    public LoginResponse() {}
    public LoginResponse(String token) {this.token = token;}
}