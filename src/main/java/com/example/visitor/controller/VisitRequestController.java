package com.example.visitor.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.visitor.entity.User;
import com.example.visitor.entity.Visitor;
import com.example.visitor.entity.VisitRequest;
import com.example.visitor.entity.VisitStatus;
import com.example.visitor.repository.UserRepository;
import com.example.visitor.repository.VisitorRepository;
import com.example.visitor.service.VisitRequestService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/visit-requests")
@RequiredArgsConstructor
public class VisitRequestController {

    private final VisitRequestService visitRequestService;
    private final UserRepository userRepository;
    private final VisitorRepository visitorRepository;

    /**
     * Create a visit request.
     * Frontend sends: { purpose, hostId, visitDate }
     * We resolve the host User by hostId, and the Visitor profile by the caller's JWT email.
     */
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody Map<String, Object> body,
                                           Authentication auth) {
        try {
            String purpose  = (String) body.get("purpose");
            Object hostIdObj = body.get("hostId");
            String visitDateStr = (String) body.get("visitDate");

            if (purpose == null || hostIdObj == null || visitDateStr == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "purpose, hostId, and visitDate are required"));
            }

            Long hostId = Long.valueOf(hostIdObj.toString());

            // Resolve host User
            User host = userRepository.findById(hostId)
                    .orElseThrow(() -> new RuntimeException("Host not found with id: " + hostId));

            // Resolve the Visitor profile of the currently logged-in user (JWT subject = email)
            String callerEmail = auth.getName();
            Visitor visitor = visitorRepository.findByUserEmail(callerEmail)
                    .orElseThrow(() -> new RuntimeException(
                        "No visitor profile found for email: " + callerEmail +
                        ". Please ensure your account was registered as a Visitor."));

            VisitRequest request = new VisitRequest();
            request.setPurpose(purpose);
            request.setHost(host);
            request.setVisitor(visitor);
            // Parse ISO datetime - handle both "2026-04-30T05:41" and "2026-04-30T05:41:00.000Z"
            String dt = visitDateStr.replace("Z", "").replace("z", "");
            if (dt.length() > 19) dt = dt.substring(0, 19); // trim millis
            request.setVisitDate(LocalDateTime.parse(dt));
            request.setStatus(VisitStatus.PENDING);

            VisitRequest saved = visitRequestService.save(request);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Keep the /request variant for backward compatibility
    @PostMapping("/request")
    public ResponseEntity<?> createRequestAlt(@RequestBody Map<String, Object> body,
                                              Authentication auth) {
        return createRequest(body, auth);
    }

    @GetMapping
    public List<VisitRequest> getAllRequests() {
        return visitRequestService.getAllRequests();
    }

    @GetMapping("/{id}")
    public VisitRequest getRequestById(@PathVariable Long id) {
        return visitRequestService.getRequestById(id);
    }

    @PutMapping("/{id}/approve")
    public VisitRequest approve(@PathVariable Long id) {
        return visitRequestService.approveRequest(id);
    }

    @PutMapping("/{id}/reject")
    public VisitRequest reject(@PathVariable Long id) {
        return visitRequestService.rejectRequest(id);
    }

    @PutMapping("/{id}/cancel")
    public VisitRequest cancel(@PathVariable Long id) {
        VisitRequest req = visitRequestService.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        req.setStatus(VisitStatus.REJECTED);
        return visitRequestService.save(req);
    }

    @PutMapping("/{id}/checkin")
    public VisitRequest checkIn(@PathVariable Long id) {
        VisitRequest req = visitRequestService.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        req.setCheckInTime(LocalDateTime.now());
        return visitRequestService.save(req);
    }

    @PutMapping("/{id}/checkout")
    public VisitRequest checkOut(@PathVariable Long id) {
        VisitRequest req = visitRequestService.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        req.setCheckOutTime(LocalDateTime.now());
        return visitRequestService.save(req);
    }

    // Old-style paths for backward compatibility
    @PutMapping("/approve/{id}")
    public VisitRequest approveAlt(@PathVariable Long id) {
        return visitRequestService.approveRequest(id);
    }

    @PutMapping("/reject/{id}")
    public VisitRequest rejectAlt(@PathVariable Long id) {
        return visitRequestService.rejectRequest(id);
    }
}
