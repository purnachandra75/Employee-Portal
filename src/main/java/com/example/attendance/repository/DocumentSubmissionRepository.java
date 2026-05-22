package com.example.attendance.repository;

import com.example.attendance.model.DocumentSubmission;
import com.example.attendance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentSubmissionRepository extends JpaRepository<DocumentSubmission, Long> {
    Optional<DocumentSubmission> findByUser(User user);
    Optional<DocumentSubmission> findByUserId(Long userId);
}
