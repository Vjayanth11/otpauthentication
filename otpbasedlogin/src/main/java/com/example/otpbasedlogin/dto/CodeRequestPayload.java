package com.example.otpbasedlogin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CodeRequestPayload {
    @NotBlank
    private String identifier;
}
