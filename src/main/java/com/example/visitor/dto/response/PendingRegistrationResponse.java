package com.example.visitor.dto.response;

import java.time.LocalDateTime;

import com.example.visitor.entity.PendingRegistration;
import com.example.visitor.entity.PendingRegistration.PendingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingRegistrationResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String phone;
    private String company;
    private PendingStatus status;
    private LocalDateTime requestedAt;

    public static PendingRegistrationResponse from(PendingRegistration p) {
        return PendingRegistrationResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .email(p.getEmail())
                .role(p.getRole().name())
                .phone(p.getPhone())
                .company(p.getCompany())
                .status(p.getStatus())
                .requestedAt(p.getRequestedAt())
                .build();
    }
}
