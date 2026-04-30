package com.example.visitor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.visitor.dto.request.LoginRequest;
import com.example.visitor.dto.request.RegisterRequest;
import com.example.visitor.dto.response.AuthResponse;
import com.example.visitor.dto.response.PendingRegistrationResponse;
import com.example.visitor.service.AuthService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** Check if an admin account already exists (public — used by frontend setup screen) */
    @GetMapping("/admin-exists")
    public ResponseEntity<Map<String, Boolean>> adminExists() {
        return ResponseEntity.ok(Map.of("exists", authService.adminExists()));
    }

    /** One-time admin account creation */
    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    /** Visitor self-registration (immediate, no approval needed) */
    @PostMapping("/register/visitor")
    public ResponseEntity<AuthResponse> registerVisitor(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerVisitor(request));
    }

    /** HOST / SECURITY registration — goes into pending state, needs admin approval */
    @PostMapping("/register/staff-request")
    public ResponseEntity<AuthResponse> requestStaffRegistration(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.requestStaffRegistration(request));
    }

    /** Admin: list all pending staff registrations */
    @GetMapping("/pending")
    public ResponseEntity<List<PendingRegistrationResponse>> getPending() {
        return ResponseEntity.ok(authService.getPendingRegistrations());
    }

    /** Admin: approve a pending registration */
    @PostMapping("/approve/{id}")
    public ResponseEntity<AuthResponse> approve(@PathVariable Long id) {
        return ResponseEntity.ok(authService.approveStaff(id));
    }

    /** Admin: reject a pending registration */
    @PostMapping("/reject/{id}")
    public ResponseEntity<AuthResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(authService.rejectStaff(id));
    }

    /** Login — works for all roles */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
