package com.example.visitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.visitor.entity.VisitRequest;

public interface VisitRequestRepository extends JpaRepository<VisitRequest, Long> {
}
