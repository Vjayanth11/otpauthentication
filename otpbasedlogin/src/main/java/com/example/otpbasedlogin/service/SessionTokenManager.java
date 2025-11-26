package com.example.otpbasedlogin.service;

import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionTokenManager {

    private final ConcurrentHashMap<String, String> tokenRegistry = new ConcurrentHashMap<>();

    public String createToken(String identifier) {
        String raw = identifier + ":" + System.currentTimeMillis();
        String token = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        tokenRegistry.put(token, identifier);
        return token;
    }

    public boolean isValid(String token) {
        return tokenRegistry.containsKey(token);
    }

    public String getIdentifier(String token) {
        return tokenRegistry.get(token);
    }
}
