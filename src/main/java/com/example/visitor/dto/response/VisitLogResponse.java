package com.example.visitor.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitLogResponse {

    private Long id;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String status;
}