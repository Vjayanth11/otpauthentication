package com.example.otpbasedlogin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_session_code")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginSessionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identifier;

    private String code;
    private LocalDateTime expiresAt;
    private int attempts;
}
