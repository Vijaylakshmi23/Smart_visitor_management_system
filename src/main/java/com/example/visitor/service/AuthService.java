package com.example.visitor.service;

import java.util.List;

import com.example.visitor.dto.request.LoginRequest;
import com.example.visitor.dto.request.RegisterRequest;
import com.example.visitor.dto.response.AuthResponse;
import com.example.visitor.dto.response.PendingRegistrationResponse;

public interface AuthService {

    AuthResponse registerAdmin(RegisterRequest request);

    AuthResponse registerVisitor(RegisterRequest request);

    AuthResponse requestStaffRegistration(RegisterRequest request); // HOST / SECURITY -> pending

    AuthResponse approveStaff(Long pendingId);

    AuthResponse rejectStaff(Long pendingId);

    List<PendingRegistrationResponse> getPendingRegistrations();

    AuthResponse login(LoginRequest request);

    boolean adminExists();
}
