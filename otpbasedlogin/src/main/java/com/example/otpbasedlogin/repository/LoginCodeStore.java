package com.example.otpbasedlogin.repository;

import com.example.otpbasedlogin.entity.LoginSessionCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginCodeStore extends JpaRepository<LoginSessionCode, Long> {
    Optional<LoginSessionCode> findByIdentifier(String identifier);
    void deleteByIdentifier(String identifier);
}
