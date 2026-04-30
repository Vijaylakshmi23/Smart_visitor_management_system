package com.example.visitor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.visitor.entity.PendingRegistration;
import com.example.visitor.entity.PendingRegistration.PendingStatus;

public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {
    Optional<PendingRegistration> findByEmail(String email);
    List<PendingRegistration> findByStatus(PendingStatus status);
}
