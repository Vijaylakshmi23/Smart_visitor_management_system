package com.example.visitor.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pending_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // already BCrypt-encoded

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role; // HOST or SECURITY only

    private String phone;
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PendingStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime reviewedAt;

    public enum PendingStatus {
        PENDING, APPROVED, REJECTED
    }
}
