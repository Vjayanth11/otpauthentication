package com.example.otpbasedlogin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CodeCheckPayload {

    @NotBlank
    private String identifier;

    @NotBlank
    private String code;
}
