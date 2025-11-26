package com.example.otpbasedlogin.repository;

import com.example.otpbasedlogin.entity.AccessBlockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRecordStore extends JpaRepository<AccessBlockRecord, Long> {
    Optional<AccessBlockRecord> findByIdentifier(String identifier);
    void deleteByIdentifier(String identifier);
}
