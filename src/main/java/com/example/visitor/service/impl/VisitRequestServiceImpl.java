package com.example.visitor.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.visitor.entity.VisitRequest;
import com.example.visitor.entity.VisitStatus;
import com.example.visitor.repository.VisitRequestRepository;
import com.example.visitor.service.VisitRequestService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitRequestServiceImpl implements VisitRequestService {

    private final VisitRequestRepository visitRequestRepository;

    @Override
    public VisitRequest createRequest(VisitRequest request) {
        request.setStatus(VisitStatus.PENDING);
        return visitRequestRepository.save(request);
    }

    @Override
    public List<VisitRequest> getAllRequests() {
        return visitRequestRepository.findAll();
    }

    @Override
    public VisitRequest getRequestById(Long id) {
        return visitRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
    }

    @Override
    public VisitRequest approveRequest(Long id) {
        VisitRequest req = visitRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus(VisitStatus.APPROVED);
        return visitRequestRepository.save(req);
    }

    @Override
    public VisitRequest rejectRequest(Long id) {
        VisitRequest req = visitRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        req.setStatus(VisitStatus.REJECTED);
        return visitRequestRepository.save(req);
    }

    @Override
    public Optional<VisitRequest> findById(Long id) {
        return visitRequestRepository.findById(id);
    }

    @Override
    public VisitRequest save(VisitRequest request) {
        return visitRequestRepository.save(request);
    }
}
