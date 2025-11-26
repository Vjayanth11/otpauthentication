package com.example.otpbasedlogin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_block_record")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccessBlockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identifier;

    private LocalDateTime blockedUntil;
}
