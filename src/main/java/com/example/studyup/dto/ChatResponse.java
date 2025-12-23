package com.example.studyup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatResponse {
    private String answer;
    private Long sessionId;
}
