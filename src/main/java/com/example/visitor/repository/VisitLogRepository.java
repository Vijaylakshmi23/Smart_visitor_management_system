package com.example.visitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.visitor.entity.VisitLog;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {
}
