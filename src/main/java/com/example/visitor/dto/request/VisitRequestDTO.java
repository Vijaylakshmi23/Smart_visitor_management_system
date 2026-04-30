package com.example.visitor.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisitRequestDTO {

    private String purpose;
    private LocalDateTime visitDate;
    private Long visitorId;
    private Long hostId;
}
