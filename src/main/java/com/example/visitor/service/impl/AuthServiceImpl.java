package com.example.visitor.service.impl;

import com.example.visitor.dto.request.LoginRequest;
import com.example.visitor.dto.request.RegisterRequest;
import com.example.visitor.dto.response.AuthResponse;
import com.example.visitor.dto.response.PendingRegistrationResponse;
import com.example.visitor.entity.*;
import com.example.visitor.entity.PendingRegistration.PendingStatus;
import com.example.visitor.repository.*;
import com.example.visitor.service.AuthService;
import com.example.visitor.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PendingRegistrationRepository pendingRepo;
    private final VisitorRepository visitorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public boolean adminExists() {
        return userRepository.existsByRole(Role.ADMIN);
    }

    @Override
    @Transactional
    public AuthResponse registerAdmin(RegisterRequest request) {
        if (adminExists()) {
            throw new RuntimeException("Admin account already exists. Only one admin is allowed.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        User admin = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .phone(request.getPhone())
                .company(request.getCompany())
                .build();

        userRepository.save(admin);

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(admin.getEmail(), Role.ADMIN.name()))
                .role(Role.ADMIN.name())
                .name(admin.getName())
                .email(admin.getEmail())
                .userId(admin.getId())
                .message("Admin account created successfully.")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse registerVisitor(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.VISITOR)
                .phone(request.getPhone())
                .company(request.getCompany())
                .build();

        userRepository.save(user);

        // Also create a Visitor profile linked to this account
        Visitor visitorProfile = Visitor.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .blacklisted(false)
                .user(user)
                .build();

        visitorRepository.save(visitorProfile);

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail(), Role.VISITOR.name()))
                .role(Role.VISITOR.name())
                .name(user.getName())
                .email(user.getEmail())
                .userId(user.getId())
                .message("Visitor account created. You can now log in.")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse requestStaffRegistration(RegisterRequest request) {
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role. Must be HOST or SECURITY.");
        }
        if (role != Role.HOST && role != Role.SECURITY) {
            throw new RuntimeException("Staff registration is only for HOST or SECURITY roles.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use by an existing account.");
        }
        if (pendingRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("A registration request for this email is already pending.");
        }

        PendingRegistration pending = PendingRegistration.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .phone(request.getPhone())
                .company(request.getCompany())
                .status(PendingStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        pendingRepo.save(pending);

        return AuthResponse.builder()
                .message("Registration request submitted. Please wait for Admin approval before logging in.")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse approveStaff(Long pendingId) {
        PendingRegistration pending = pendingRepo.findById(pendingId)
                .orElseThrow(() -> new RuntimeException("Pending registration not found."));

        if (pending.getStatus() != PendingStatus.PENDING) {
            throw new RuntimeException("This request has already been " + pending.getStatus().name().toLowerCase() + ".");
        }
        if (userRepository.findByEmail(pending.getEmail()).isPresent()) {
            throw new RuntimeException("An account with this email already exists.");
        }

        User user = User.builder()
                .name(pending.getName())
                .email(pending.getEmail())
                .password(pending.getPassword())
                .role(pending.getRole())
                .phone(pending.getPhone())
                .company(pending.getCompany())
                .build();

        userRepository.save(user);

        pending.setStatus(PendingStatus.APPROVED);
        pending.setReviewedAt(LocalDateTime.now());
        pendingRepo.save(pending);

        return AuthResponse.builder()
                .message("Account approved. " + user.getName() + " can now log in.")
                .build();
    }

    @Override
    @Transactional
    public AuthResponse rejectStaff(Long pendingId) {
        PendingRegistration pending = pendingRepo.findById(pendingId)
                .orElseThrow(() -> new RuntimeException("Pending registration not found."));

        if (pending.getStatus() != PendingStatus.PENDING) {
            throw new RuntimeException("This request has already been " + pending.getStatus().name().toLowerCase() + ".");
        }

        pending.setStatus(PendingStatus.REJECTED);
        pending.setReviewedAt(LocalDateTime.now());
        pendingRepo.save(pending);

        return AuthResponse.builder()
                .message("Registration request for " + pending.getName() + " has been rejected.")
                .build();
    }

    @Override
    public List<PendingRegistrationResponse> getPendingRegistrations() {
        return pendingRepo.findByStatus(PendingStatus.PENDING)
                .stream()
                .map(PendingRegistrationResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail(), user.getRole().name()))
                .role(user.getRole().name())
                .name(user.getName())
                .email(user.getEmail())
                .userId(user.getId())
                .message("Login successful.")
                .build();
    }
}
