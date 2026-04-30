package com.example.visitor.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String role;   // "ADMIN", "HOST", "SECURITY", "VISITOR"
    private String phone;
    private String company;
}
