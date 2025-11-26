package com.example.otpbasedlogin.controller;

import com.example.otpbasedlogin.dto.CodeRequestPayload;
import com.example.otpbasedlogin.dto.CodeCheckPayload;
import com.example.otpbasedlogin.dto.LoginTokenResponse;
import com.example.otpbasedlogin.service.LoginCodeManager;
import com.example.otpbasedlogin.service.SessionTokenManager;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/access")
public class AccessController {

    private final LoginCodeManager codeManager;
    private final SessionTokenManager tokenManager;

    public AccessController(LoginCodeManager codeManager,
                            SessionTokenManager tokenManager) {
        this.codeManager = codeManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestCode(@Valid @RequestBody CodeRequestPayload payload) {
        codeManager.sendCode(payload);
        return ResponseEntity.ok("Code sent successfully.");
    }

    @PostMapping("/verify")
    public ResponseEntity<LoginTokenResponse> verify(@Valid @RequestBody CodeCheckPayload payload) {
        return ResponseEntity.ok(codeManager.verify(payload));
    }

    @GetMapping("/me")
    public ResponseEntity<?> profile(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authHeader.substring(7);

        if (!tokenManager.isValid(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        String id = tokenManager.getIdentifier(token);

        return ResponseEntity.ok(
                new java.util.HashMap<>() {{
                    put("identifier", id);
                    put("status", "Authenticated");
                }}
        );
    }
}
