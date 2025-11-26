package com.example.otpbasedlogin.service;

import com.example.otpbasedlogin.dto.CodeRequestPayload;
import com.example.otpbasedlogin.dto.CodeCheckPayload;
import com.example.otpbasedlogin.dto.LoginTokenResponse;
import com.example.otpbasedlogin.entity.AccessBlockRecord;
import com.example.otpbasedlogin.entity.LoginSessionCode;
import com.example.otpbasedlogin.repository.BlockRecordStore;
import com.example.otpbasedlogin.repository.LoginCodeStore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class LoginCodeManager {

    private final LoginCodeStore codeStore;
    private final BlockRecordStore blockStore;
    private final SessionTokenManager tokenManager;

    @Value("${otp.expiry.minutes}")
    private int expiryMinutes;

    @Value("${otp.max-attempts}")
    private int maxAttempts;

    @Value("${otp.block-minutes}")
    private int blockMinutes;

    public LoginCodeManager(LoginCodeStore codeStore,
                            BlockRecordStore blockStore,
                            SessionTokenManager tokenManager) {
        this.codeStore = codeStore;
        this.blockStore = blockStore;
        this.tokenManager = tokenManager;
    }

    public void sendCode(CodeRequestPayload payload) {
        String id = payload.getIdentifier().trim().toLowerCase();

        var block = blockStore.findByIdentifier(id);
        if (block.isPresent() && block.get().getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Identifier blocked until " + block.get().getBlockedUntil());
        }

        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(expiryMinutes);

        LoginSessionCode entry = codeStore.findByIdentifier(id)
                .map(e -> {
                    e.setCode(code);
                    e.setAttempts(0);
                    e.setExpiresAt(expiry);
                    return e;
                })
                .orElse(LoginSessionCode.builder()
                        .identifier(id)
                        .code(code)
                        .attempts(0)
                        .expiresAt(expiry)
                        .build()
                );

        codeStore.save(entry);

        System.out.println("[Mock Code Sent] " + id + " -> " + code);
    }

    @Transactional
    public LoginTokenResponse verify(CodeCheckPayload payload) {

        String id = payload.getIdentifier().trim().toLowerCase();
        String input = payload.getCode().trim();

        var block = blockStore.findByIdentifier(id);
        if (block.isPresent() && block.get().getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Identifier blocked until " + block.get().getBlockedUntil());
        }

        LoginSessionCode entry = codeStore.findByIdentifier(id)
                .orElseThrow(() -> new IllegalArgumentException("No active login request"));

        if (entry.getExpiresAt().isBefore(LocalDateTime.now())) {
            codeStore.delete(entry);
            throw new IllegalArgumentException("Code expired. Request new code.");
        }

        if (!entry.getCode().equals(input)) {
            entry.setAttempts(entry.getAttempts() + 1);
            codeStore.save(entry);

            if (entry.getAttempts() >= maxAttempts) {
                blockStore.save(AccessBlockRecord.builder()
                        .identifier(id)
                        .blockedUntil(LocalDateTime.now().plusMinutes(blockMinutes))
                        .build());
                codeStore.deleteByIdentifier(id);
                throw new IllegalStateException("Too many attempts. You are blocked.");
            }

            throw new IllegalArgumentException("Incorrect code. Try again.");
        }

        codeStore.deleteByIdentifier(id);

        String token = tokenManager.createToken(id);

        return new LoginTokenResponse(token, "Login successful");
    }
}
