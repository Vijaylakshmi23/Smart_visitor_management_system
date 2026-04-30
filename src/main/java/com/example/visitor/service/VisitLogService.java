package com.example.visitor.service;

import java.util.List;

import com.example.visitor.entity.VisitLog;

public interface VisitLogService {

    VisitLog checkIn(Long visitRequestId);

    VisitLog checkOut(Long visitLogId);

    List<VisitLog> getAllLogs();

    VisitLog getLogById(Long id);
}
