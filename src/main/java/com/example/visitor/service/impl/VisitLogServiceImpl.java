package com.example.visitor.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.visitor.entity.VisitLog;
import com.example.visitor.entity.VisitLogStatus;
import com.example.visitor.entity.VisitRequest;
import com.example.visitor.repository.VisitLogRepository;
import com.example.visitor.repository.VisitRequestRepository;
import com.example.visitor.service.VisitLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitLogServiceImpl implements VisitLogService {

    private final VisitLogRepository visitLogRepository;
    private final VisitRequestRepository visitRequestRepository;

    @Override
    public VisitLog checkIn(Long visitRequestId) {

        VisitRequest request = visitRequestRepository.findById(visitRequestId)
                .orElseThrow(() -> new RuntimeException("Visit request not found"));

        VisitLog log = VisitLog.builder()
                .visitRequest(request)
                .checkInTime(LocalDateTime.now())
                .status(VisitLogStatus.ACTIVE)
                .build();

        return visitLogRepository.save(log);
    }

    @Override
    public VisitLog checkOut(Long visitLogId) {

        VisitLog log = visitLogRepository.findById(visitLogId)
                .orElseThrow(() -> new RuntimeException("Visit log not found"));

        log.setCheckOutTime(LocalDateTime.now());
        log.setStatus(VisitLogStatus.COMPLETED);

        return visitLogRepository.save(log);
    }

    @Override
    public List<VisitLog> getAllLogs() {
        return visitLogRepository.findAll();
    }

    @Override
    public VisitLog getLogById(Long id) {
        return visitLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit log not found"));
    }
}
