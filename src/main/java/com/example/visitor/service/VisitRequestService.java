package com.example.visitor.service;

import java.util.List;
import java.util.Optional;

import com.example.visitor.entity.VisitRequest;

public interface VisitRequestService {

    VisitRequest createRequest(VisitRequest request);

    List<VisitRequest> getAllRequests();

    VisitRequest getRequestById(Long id);

    VisitRequest approveRequest(Long id);

    VisitRequest rejectRequest(Long id);

    Optional<VisitRequest> findById(Long id);

    VisitRequest save(VisitRequest request);

}
