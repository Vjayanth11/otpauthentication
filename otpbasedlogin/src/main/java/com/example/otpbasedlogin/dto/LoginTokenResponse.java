package com.example.otpbasedlogin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginTokenResponse {
    private String token;
    private String message;
}
